package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.po.OAuthInfo;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OauthInfoMapper {
    OAuthInfo WxMpOAuth2AccessTokenToOAuthInfo(WxMpOAuth2AccessToken oAuthInfo);
}