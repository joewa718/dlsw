package com.dlsw.cn.web.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author zhanwang
 * @create 2017-10-17 9:52
 **/
public class SpringAuthenticationProvider extends DaoAuthenticationProvider {
    private boolean isAvoidPassword = false;

    public boolean isAvoidPassword() {
        return isAvoidPassword;
    }

    public void setAvoidPassword(boolean avoidPassword) {
        isAvoidPassword = avoidPassword;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if(!isAvoidPassword){
            super.additionalAuthenticationChecks(userDetails, authentication);
        }
    }
}
