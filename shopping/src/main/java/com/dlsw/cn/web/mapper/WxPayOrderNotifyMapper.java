package com.dlsw.cn.web.mapper;

import com.dlsw.cn.web.po.WxPayOrderNotify;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WxPayOrderNotifyMapper {
    WxPayOrderNotify WxPayOrderNotifyResultToWxPayOrderNotify(WxPayOrderNotifyResult wxPayOrderNotifyResult);
}