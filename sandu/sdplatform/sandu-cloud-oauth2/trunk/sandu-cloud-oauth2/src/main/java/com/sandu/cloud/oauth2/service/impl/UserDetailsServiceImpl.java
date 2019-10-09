package com.sandu.cloud.oauth2.service.impl;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.oauth2.client.UserServiceClient;
import com.sandu.cloud.user.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user = userServiceClient.getByOpenId(username);
    	LoginUser loginUser = new LoginUser(user.getOpenId(),
    			user.getPassword()==null?"123456":user.getPassword(),//"$2a$10$zL1iBEfbgmC7molTRKOysOE8iSuS6LY5iu3LW2XFYAiLOlbHw8lVG",
    			 AuthorityUtils.commaSeparatedStringToAuthorityList("pay_test"));
    	BeanUtils.copyProperties(user, loginUser);
    	return loginUser;
    }
}
