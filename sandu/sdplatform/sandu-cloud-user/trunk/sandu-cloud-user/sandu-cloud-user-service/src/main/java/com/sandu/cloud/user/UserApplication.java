package com.sandu.cloud.user;

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
@EnableFeignClients
@SpringBootApplication(scanBasePackages={"com.sandu.cloud.user","com.sandu.cloud.common"})
@MapperScan("com.sandu.cloud.user.dao")
public class UserApplication 
{
    public static void main( String[] args ) throws UnknownHostException
    {
    	InetAddress addr = InetAddress.getLocalHost();  
	    String ip=addr.getHostAddress().toString(); //获取本机ip  
	    //String hostName=addr.getHostName().toString(); //获取本机计算机名称  
		System.setProperty("spring.cloud.client.ip", ip);
    	SpringApplication.run(UserApplication.class, args);
    }
}