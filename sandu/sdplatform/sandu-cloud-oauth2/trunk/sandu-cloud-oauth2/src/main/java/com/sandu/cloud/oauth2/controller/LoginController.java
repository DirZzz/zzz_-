package com.sandu.cloud.oauth2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandu.cloud.common.authentication.openid.OpenIdAuthenticationToken;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.oauth2.client.UserServiceClient;
import com.sandu.cloud.oauth2.exception.OauthBizException;
import com.sandu.cloud.oauth2.exception.OauthExceptionCode;
import com.sandu.cloud.oauth2.service.impl.UserDetailsServiceImpl;
import com.sandu.cloud.user.model.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/oauth2")
@Slf4j
public class LoginController {
	
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
	private AuthorizationServerTokenServices tokenServices;

	@Autowired
	UserServiceClient userServiceClient;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping(path = "/ssoLogin")
	public void postAccessToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameters) throws Exception {
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Basic ")) {
			throw new OauthBizException(OauthExceptionCode.SD_ERR_OAUTH_CLIENT_INFO_PARAM_ERROR);
		}
		
		String[] tokens = extractAndDecodeHeader(header, request);
		if(tokens.length != 2) {
			throw new OauthBizException(OauthExceptionCode.SD_ERR_OAUTH_TOKEN_ERROR);
		}

		String clientId = tokens[0];
		String clientSecret = tokens[1];
		
		ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
		
		if (authenticatedClient == null) {
			throw new OauthBizException(OauthExceptionCode.SD_ERR_OAUTH_CLIENT_INFO_NOTFOUND);
		} else if (!passwordEncoder.matches(clientSecret, authenticatedClient.getClientSecret())) {
			throw new OauthBizException(OauthExceptionCode.SD_ERR_OAUTH_CLIENT_INFO_PASSWORD_ERROR);
		}
		
		
		TokenRequest tokenRequest = new TokenRequest(parameters, clientId, authenticatedClient.getScope(), "sso");
		
		OAuth2Request storedOAuth2Request = tokenRequest.createOAuth2Request(authenticatedClient);
		UserDetails loginUser = userDetailsServiceImpl.loadUserByUsername(parameters.get("openId"));
		OpenIdAuthenticationToken openIdAuthenticationToken = new OpenIdAuthenticationToken(loginUser, loginUser.getAuthorities());
		OAuth2AccessToken oauth2AccessToken = tokenServices.createAccessToken(new OAuth2Authentication(storedOAuth2Request, openIdAuthenticationToken));
		System.out.println(">>>>>>>>>>>>>>>>>"+oauth2AccessToken);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(oauth2AccessToken));
	}
	
	/**
	 * Decodes the header into a username and password.
	 *
	 * @throws BadCredentialsException if the Basic header is not present or is not valid
	 * Base64
	 */
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
			throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		}
		catch (IllegalArgumentException e) {
			throw new BadCredentialsException(
					"Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}
	
	protected String getClientId(Principal principal) {
		Authentication client = (Authentication) principal;
		if (!client.isAuthenticated()) {
			throw new InsufficientAuthenticationException("The client is not authenticated.");
		}
		String clientId = client.getName();
		if (client instanceof OAuth2Authentication) {
			// Might be a client and user combined authentication
			clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
		}
		return clientId;
	}
}
