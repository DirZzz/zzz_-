package com.sandu.cloud.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigApplication {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();  
	    String ip=addr.getHostAddress().toString(); //获取本机ip  
	    //String hostName=addr.getHostName().toString(); //获取本机计算机名称  
		System.setProperty("spring.cloud.client.ip", ip);
		SpringApplication.run(ConfigApplication.class, args);
	}
}