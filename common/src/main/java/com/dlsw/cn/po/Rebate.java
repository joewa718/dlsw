package com.dlsw.cn.po;/**
 * Created by zhanwa01 on 2017/9/24.
 */

import com.dlsw.cn.converter.RebateStatusConverter;
import com.dlsw.cn.enumerate.RebateStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author zhanwang
 * @create 2017-09-24 13:08
 **/
@Entity
@Table(name = "t_rebate")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class Rebate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "rebate")
    private BigDecimal rebate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "reason")
    private String reason;
    @Convert(converter = RebateStatusConverter.class)
    @Column(name = "rebate_status")
    private RebateStatus rebateStatus;

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
}
