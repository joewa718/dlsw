package com.dlsw.cn.web.service;

import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.User;

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
     * 级差返利
     * @param recommend
     * @param order
     */
    void calLevelRebate(User recommend, Order order);

    /**
     * 计算返利
     */
    void calRebate(Order order);
}
