package com.dlsw.cn.web.mapper;

import com.dlsw.cn.web.dto.UserDTO;
import com.dlsw.cn.web.po.User;
import com.dlsw.cn.web.vo.UserVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userToUserVo(UserVo userVo);

    List<UserDTO> userToUserDTOList(List<User> userList);

    List<UserDTO> userToUserDTOList(Iterable<User> userList);
}