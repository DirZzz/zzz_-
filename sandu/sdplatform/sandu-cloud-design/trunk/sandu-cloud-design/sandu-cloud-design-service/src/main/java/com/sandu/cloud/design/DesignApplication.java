package com.sandu.cloud.design;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages={"com.sandu.cloud.design","com.sandu.cloud.common"})
@EnableFeignClients
@MapperScan("com.sandu.cloud.design.dao")
public class DesignApplication {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();  
	    String ip=addr.getHostAddress().toString(); //获取本机ip  
	    //String hostName=addr.getHostName().toString(); //获取本机计算机名称  
		System.setProperty("spring.cloud.client.ip", ip);
		SpringApplication.run(DesignApplication.class, args);
	}
}
