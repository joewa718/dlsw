package com.dlsw.cn.vo;

import com.dlsw.cn.enumerate.OrderStatus;
import com.dlsw.cn.po.User;

/**
 * @author zhanwang
 * @create 2017-08-11 21:00
 **/
public class OrderVo extends PageVo{

    private String orderCode;
    private OrderStatus orderStatus;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
