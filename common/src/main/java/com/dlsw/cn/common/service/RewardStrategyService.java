package com.dlsw.cn.common.service;

import com.dlsw.cn.common.po.Order;

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
