package com.dlsw.cn.shopping.service;

import com.dlsw.cn.common.po.DeliveryAddress;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.Product;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.shopping.vo.OrderVo;
import com.dlsw.cn.shopping.vo.PayCertificateVo;

public interface OrderCheckService {

    void applyOrder(User user, Product product, User recommend_man, DeliveryAddress deliveryAddress, OrderVo orderVo);

    void savePayCert(Order order, PayCertificateVo payCertificateVo);

    void sureOrder(Order order, User user, User orderUser);
}
