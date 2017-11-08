package com.dlsw.cn.service;

import com.dlsw.cn.po.Order;

public interface PromoteService {

    void monitorVip(Order order);

    void monitorPartner(Order order);
}
