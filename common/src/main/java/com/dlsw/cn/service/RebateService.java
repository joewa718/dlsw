package com.dlsw.cn.service;

import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.User;

import java.util.List;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
public interface RebateService {


    /**
     * 高级平级返利 12%
     * @param recommend user,
     * @param order
     */
    void calSeniorRebate(User recommend, Order order);

    /**
     * 信平级返利 4%
     * @param recommend user,
     * @param order
     */
    void calCreditRebate(User recommend, Order order);

    /**
     * 计算返利
     */
    void calRebate(Order order);
}
