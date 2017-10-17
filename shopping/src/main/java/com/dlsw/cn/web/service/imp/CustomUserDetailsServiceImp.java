package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.dto.UserSecurityDTO;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsServiceImp implements UserDetailsService {
    @Value("${wechat.login.defaultPwd}")
    private String defaultPwd;
    //普通用户权限
    public final static String ROLE_USER = "ROLE_USER";
    //管理员权限
    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserSecurityDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneOrAppId(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_USER));
        return new UserSecurityDTO(user.getPhone(), user.getPassword(), authorities, user);
    }

}
