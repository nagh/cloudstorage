package client;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.RunInstancesRequest;


public class EC2Instances {
	
	
	private static String s3_folder = "cc-ue3-ac";
	
	public static void main(String[] args) throws Exception {
		AWSCredentials credentials = new PropertiesCredentials(new File("src/client/AwsCredentials.properties"));
		AmazonEC2 ec2 = new AmazonEC2Client(credentials);
		
		// Master
		ec2.setEndpoint("ec2.eu-west-1.amazonaws.com");
		RunInstancesRequest Master = new RunInstancesRequest();
		// List list = new ArrayList();
		// collection.add("CC-ue3-ac");
		// collection.add("default");
		Master.withImageId("ami-14907e63")
				 .withInstanceType("m1.small")
				 .withMinCount(1)  // number of instances to be started at least
                 .withMaxCount(1)  // number of instances to be started at max
		         .withKeyName("CC-ue3-ac-eu")
		         .withSecurityGroups("CC-ue3-ac")
		         .withUserData(getUserData("true","true"));
		ec2.runInstances(Master);
		// Slave 1
		ec2.setEndpoint("ec2.us-east-1.amazonaws.com");
		RunInstancesRequest Slave1 = new RunInstancesRequest();
		Slave1.withImageId("ami-bd6d40d4")
				 .withInstanceType("m1.small")
				 .withMinCount(1)  // number of instances to be started at least
                 .withMaxCount(1)  // number of instances to be started at max
		         .withKeyName("CC-ue3-ac-use")
		         .withSecurityGroups("CC-ue3-ac")
		         
				 .withUserData(getUserData("false","true"));
		ec2.runInstances(Slave1);
		// Slave 2
		ec2.setEndpoint("ec2.us-west-1.amazonaws.com");
		RunInstancesRequest Slave2 = new RunInstancesRequest();
		Slave2.withImageId("ami-668dbc23")
				 .withInstanceType("m1.small")
				 .withMinCount(1)  // number of instances to be started at least
                 .withMaxCount(1)  // number of instances to be started at max
		         .withKeyName("CC-ue3-ac-usw")
		         .withSecurityGroups("CC-ue3-ac")
				 .withUserData(getUserData("false","false"));
		ec2.runInstances(Slave2);		
		
	}
	
	private static String getUserData(String isMaster, String isSlave) {
		String USER_DATA = //
				"#!/bin/bash \n" //
				+ "set -e -x \n" //
				+ "export DEBIAN_FRONTEND=noninteractive \n" //
				+ "export EC2_INSTANCE_ID=\"`wget -q -O - http://169.254.169.254/latest/meta-data/instance-id`\" \n" //
				+ "echo EC2_INSTANCE_ID=$EC2_INSTANCE_ID >> /etc/environment \n" //
				+ "source /etc/environment \n" //
				+ "apt-get update -y \n" //
				+ "wget https://gist.github.com/bugra-derre/7212270/raw/2a11b0f1af11d531332cc39318ae5111a159b09d/install_java_from_url.sh \n" //
				+ "sh install_java_from_url.sh 7 https://s3.amazonaws.com/edu.kit.aifb.eorg.derre/jdk-7u45-linux-i586.tar.gz \n" //
				+ "cd /home/ubuntu \n"//
				+ "wget https://s3.amazonaws.com/" + s3_folder + "/ServerMain.jar \n"//
				+ "nohup java -jar ServerMain.jar " + isMaster + " " + isSlave;//	
		
		byte[] encodeBase64 = Base64.encodeBase64(USER_DATA.getBytes());
		return new String(encodeBase64);
	}
}
