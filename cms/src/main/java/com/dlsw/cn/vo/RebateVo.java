package com.dlsw.cn.vo;

import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.po.Rebate;

/**
 * @author zhanwang
 * @create 2017-10-01 10:44
 **/
public class RebateVo extends PageVo{

    private String orderCode;
    private String yearMonth;
    private RebateStatus rebateStatus;

    public RebateStatus getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(RebateStatus rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
