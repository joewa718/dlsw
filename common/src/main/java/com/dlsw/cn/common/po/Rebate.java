package com.dlsw.cn.common.po;

import com.dlsw.cn.common.converter.RebateStatusConverter;
import com.dlsw.cn.common.converter.RebateTypeConverter;
import com.dlsw.cn.common.enumerate.RebateStatus;
import com.dlsw.cn.common.enumerate.RebateType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhanwang
 * @create 2017-09-24 13:08
 **/
@Entity
@Table(name = "t_rebate")
public class Rebate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "rebate")
    private BigDecimal rebate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "reason")
    private String reason;
    @Convert(converter = RebateTypeConverter.class)
    @Column(name = "rebateType")
    private RebateType rebateType;
    @Convert(converter = RebateStatusConverter.class)
    @Column(name = "rebate_status")
    private RebateStatus rebateStatus;
    @Column(name = "rebate_time")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date rebateTime;

    public Date getRebateTime() {
        return rebateTime;
    }

    public void setRebateTime(Date rebateTime) {
        this.rebateTime = rebateTime;
    }

    public RebateStatus getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(RebateStatus rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RebateType getRebateType() {
        return rebateType;
    }

    public void setRebateType(RebateType rebateType) {
        this.rebateType = rebateType;
    }
}
