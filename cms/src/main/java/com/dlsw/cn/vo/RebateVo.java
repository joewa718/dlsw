package com.dlsw.cn.vo;
/**
 * @author zhanwang
 * @create 2017-10-01 10:44
 **/
public class RebateVo extends PageVo{

    private String orderCode;
    private String yearMonth;

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
