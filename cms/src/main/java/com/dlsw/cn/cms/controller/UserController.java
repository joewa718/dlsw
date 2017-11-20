package com.dlsw.cn.cms.controller;

import com.dlsw.cn.cms.service.UserService;
import com.dlsw.cn.common.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login");
        requestDispatcher.forward(request, response);
    }

    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> getLoginUser() {
        String phone = super.getCurrentUser().getUsername();
        UserDTO user = userService.findUserDTOByPhone(phone);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
