package com.dlsw.cn.service;

import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.User;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
public interface RebateService {


    /**
     * 高级平级返利 12%
     * @param user
     * @param order
     */
    void calSeniorRebate(User user, Order order);

    /**
     * 信平级返利 4%
     * @param user
     * @param order
     */
    void calCreditRebate(User user, Order order);

    /**
     * 计算团队返利，按照极差计算
     */
    void calTeamRebate();
}
