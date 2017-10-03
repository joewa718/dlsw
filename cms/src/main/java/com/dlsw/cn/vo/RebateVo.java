package com.dlsw.cn.vo;

import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.po.Rebate;
import io.swagger.annotations.ApiParam;

/**
 * @author zhanwang
 * @create 2017-10-01 10:44
 **/
public class RebateVo extends PageVo{
    private String orderCode;
    @ApiParam(value = "月份或日期：格式YYYY-MM或YYYY-MM-DD")
    private String searchDate;
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

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }
}
