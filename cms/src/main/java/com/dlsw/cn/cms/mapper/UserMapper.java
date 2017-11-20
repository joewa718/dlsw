package com.dlsw.cn.cms.mapper;

import com.dlsw.cn.cms.vo.UserVo;
import com.dlsw.cn.common.dto.UserDTO;
import com.dlsw.cn.common.po.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userToUserVo(UserVo userVo);

    List<UserDTO> userToUserDTOList(List<User> userList);

    List<UserDTO> userToUserDTOList(Iterable<User> userList);
}