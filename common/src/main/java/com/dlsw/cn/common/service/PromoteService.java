package com.dlsw.cn.common.service;

import com.dlsw.cn.common.po.Order;

public interface PromoteService {

    void monitorVip(Order order);

    void monitorPartner(Order order);
}
