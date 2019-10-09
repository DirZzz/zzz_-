package com.sandu.cloud.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.sandu.cloud.oauth2.service.impl.UserDetailsServiceImpl;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    TokenStore tokenStore;
    
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		
	}

	/**
	 * 客户端列表配置:包括client_id,client_secret,redirect_url等
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
        .withClient("ClientId")
        .secret("$2a$10$zL1iBEfbgmC7molTRKOysOE8iSuS6LY5iu3LW2XFYAiLOlbHw8lVG")
     //   .accessTokenValiditySeconds(600000)
     //   .refreshTokenValiditySeconds(600000)
        .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password")
        .scopes("app");
	}
	
 
   

	/**
	 * 授权服务器配置:
	 * 	1.访问用户账号的接口
	 *  2.token生成
	 *  3.authenticationManager(spring security的认证管理器)
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
        .tokenStore(tokenStore)
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsServiceImpl);
	}



}
