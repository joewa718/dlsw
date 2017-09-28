package com.dlsw.cn.web.controller;

import com.dlsw.cn.web.service.UserService;
import com.dlsw.cn.web.util.GenerateRandomCode;
import com.dlsw.cn.po.User;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpUserQuery;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 *  注意：此contorller 实现WxMpMenuService接口，仅是为了演示如何调用所有菜单相关操作接口，
 *      实际项目中无需这样，根据自己需要添加对应接口即可
 * </pre>
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Controller
@RequestMapping("/api/wechat/user/")
public class WeLoginController extends WxMpUserQuery {
    private static final String DEFAULT_PWD="~!@Wz718718";
    private static final String LOGIN_CALLBACK="http://www.jinhuishengwu.cn/api/wechat/user/weLoginCallback";
    private static final String LOGIN_SUCCESS="http://www.jinhuishengwu.cn/u.html";
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private final static Logger log = LoggerFactory.getLogger(WeLoginController.class);
    @Autowired
    private WxMpService wxService;
    @Autowired
    private UserService userServiceImp;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenerateRandomCode generateRandomCode =new GenerateRandomCode();
        String connectUrl = wxService.oauth2buildAuthorizationUrl( LOGIN_CALLBACK, "snsapi_userinfo", generateRandomCode.generate(5));
        response.sendRedirect(connectUrl);
    }

    @ApiOperation(value = "用户登录回调")
    @RequestMapping(value = "/weLoginCallback", method = RequestMethod.GET)
    public void weLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            User user = userServiceImp.regWxUser(wxMpOAuth2AccessToken,wxMpUser);
            userServiceImp.setWxLogin(user.getAppId(),true);
            Authentication token = new UsernamePasswordAuthenticationToken(user.getAppId(), DEFAULT_PWD);
            Authentication result =daoAuthenticationProvider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(result);
            userServiceImp.setWxLogin(user.getAppId(),false);
            response.sendRedirect(LOGIN_SUCCESS);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
        }
    }

}