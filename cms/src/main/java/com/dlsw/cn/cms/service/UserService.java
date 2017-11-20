package com.dlsw.cn.cms.service;

import com.dlsw.cn.common.dto.UserDTO;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface UserService {


    UserDTO findUserDTOByPhone(String phone);

}
