package com.dlsw.cn.service;

import com.dlsw.cn.po.Rebate;

import java.util.List;

/**
 * @author zhanwang
 * @create 2017-09-30 15:14
 **/
public interface RebateService {

    List<Rebate> getRebateList(Rebate rebate);

    void updateRebate(List<Long> ids);
}
