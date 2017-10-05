package com.dlsw.cn.service;

import com.dlsw.cn.dto.OrderDTO;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {


    /**
     * 获得订单详细信息
     *
     * @param phone
     * @param orderId
     * @return
     */
    OrderDTO getOrder(String phone, long orderId);

}
