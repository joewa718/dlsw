package com.dlsw.cn.web.controller;

import com.dlsw.cn.util.encrypt.AESCryptUtil;
import com.dlsw.cn.web.service.UserService;
import com.dlsw.cn.util.GenerateRandomCode;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final static Logger log = LoggerFactory.getLogger(WeLoginController.class);
    @Value("${wechat.login.defaultPwd}")
    private String defaultPwd;
    @Value("${wechat.login.loginCallback}")
    private String loginCallback;
    @Value("${wechat.login.bindPhonePath}")
    private String bindPhonePath;
    @Value("${wechat.login.loginSuccessPath}")
    private String loginSuccessPath;
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    private WxMpService wxService;
    @Autowired
    private UserService userServiceImp;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenerateRandomCode generateRandomCode = new GenerateRandomCode();
        String connectUrl = wxService.oauth2buildAuthorizationUrl(defaultPwd, "snsapi_userinfo", generateRandomCode.generate(5));
        response.sendRedirect(connectUrl);
    }

    @ApiOperation(value = "用户登录回调")
    @RequestMapping(value = "/weLoginCallback", method = RequestMethod.GET)
    public void weLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state,
                                HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser wxMpUser = new WxMpUser();
        wxMpUser.setOpenId("oTQL_wV1ffX0EEf0kpFhaquV_qy4");
        /*WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
        WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");*/
        User user = userServiceImp.regWxUser(wxMpOAuth2AccessToken, wxMpUser);
        if(user.getPhone() == null){
            response.sendRedirect(bindPhonePath + "?id=" + user.getId());
        }else{
            try{
                Authentication result = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getPhone(), AESCryptUtil.decrypt(user.getPassword())));
                SecurityContextHolder.getContext().setAuthentication(result);
                response.sendRedirect(loginSuccessPath);
            }catch(AuthenticationException e){
                log.error(e.getMessage(),e);
                throw e;
            }
        }

    }
}
