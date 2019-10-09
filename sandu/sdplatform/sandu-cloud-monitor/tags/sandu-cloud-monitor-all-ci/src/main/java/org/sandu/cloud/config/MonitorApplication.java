package org.sandu.cloud.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;


@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbine
@EnableHystrixDashboard
public class MonitorApplication {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();  
	    String ip=addr.getHostAddress().toString(); //获取本机ip  
	    //String hostName=addr.getHostName().toString(); //获取本机计算机名称  
		System.setProperty("spring.cloud.client.ip", ip);
		SpringApplication.run(MonitorApplication.class, args);
	}
	
	/*@Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()  
                .and().csrf().disable();
        }
    }*/
}

