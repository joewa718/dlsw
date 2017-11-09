package com.dlsw.cn.web.service;

import com.dlsw.cn.web.po.Order;

public interface PromoteService {

    void monitorVip(Order order);

    void monitorPartner(Order order);
}
