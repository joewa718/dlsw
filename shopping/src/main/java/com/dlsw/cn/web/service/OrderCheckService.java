package com.dlsw.cn.web.service;

import com.dlsw.cn.web.po.DeliveryAddress;
import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.Product;
import com.dlsw.cn.web.po.User;
import com.dlsw.cn.web.vo.OrderVo;
import com.dlsw.cn.web.vo.PayCertificateVo;

public interface OrderCheckService {

    void applyOrder(User user, Product product, User recommend_man, DeliveryAddress deliveryAddress, OrderVo orderVo);

    void savePayCert(Order order, PayCertificateVo payCertificateVo);

    void sureOrder(Order order, User user, User orderUser);

    boolean gtLevelSelf(User user, User recommend_man);
}
