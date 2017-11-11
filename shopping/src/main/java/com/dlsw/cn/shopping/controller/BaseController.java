package com.dlsw.cn.shopping.controller;

import com.dlsw.cn.common.dto.UserSecurityDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhanWang on 2016/03/17.
 */
public class BaseController {
    protected UserSecurityDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserSecurityDTO) principal);
        }
        return new UserSecurityDTO();
    }

}
