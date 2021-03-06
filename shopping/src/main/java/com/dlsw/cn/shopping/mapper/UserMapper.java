package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.dto.UserDTO;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.shopping.vo.UserVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userToUserVo(UserVo userVo);

    List<UserDTO> userToUserDTOList(List<User> userList);

    List<UserDTO> userToUserDTOList(Iterable<User> userList);
}