package com.dlsw.cn.cms.service.imp;

import com.dlsw.cn.cms.mapper.UserMapper;
import com.dlsw.cn.cms.service.UserService;
import com.dlsw.cn.common.dto.UserDTO;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
@Service
@Scope("prototype")
public class UserServiceImp extends BaseService implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDTO findUserDTOByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return userMapper.userToUserDTO(user);
    }
}
