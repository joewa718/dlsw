package com.dlsw.cn.dto;

/**
 * Created by zhanwa01 on 2017/8/9.
 */

import com.dlsw.cn.converter.RebateStatusConverter;
import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhanwang
 * @create 2017-08-09 11:18
 **/
public class RebateDTO {

    private long id;

    private BigDecimal rebate;

    private String username;

    private String orderCode;

    private String reason;

    private String rebateStatus;

    protected Date rebateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getRebateTime() {
        return rebateTime;
    }

    public void setRebateTime(Date rebateTime) {
        this.rebateTime = rebateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
