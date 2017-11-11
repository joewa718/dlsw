package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.po.WxPayOrderNotify;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WxPayOrderNotifyMapper {
    WxPayOrderNotify WxPayOrderNotifyResultToWxPayOrderNotify(WxPayOrderNotifyResult wxPayOrderNotifyResult);
}