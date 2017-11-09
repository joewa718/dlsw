package com.dlsw.cn.web.service;

import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.User;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
public interface RewardStrategyService {
    /**
     * 计算返利
     */
    void calRebate(Order order);
}
