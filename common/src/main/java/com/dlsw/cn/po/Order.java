package com.dlsw.cn.po;

import com.dlsw.cn.converter.OrderStatusConverter;
import com.dlsw.cn.converter.PayTypeConverter;
import com.dlsw.cn.enumerate.OrderStatus;
import com.dlsw.cn.enumerate.PayType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zhanwang
 * @create 2017-08-11 17:19
 **/
@Entity
@Table(name = "t_order")
public class Order extends BasePo{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "order_code")
    private String orderCode;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private String receiverPhone;
    @Column(name = "receiver_province")
    private String receiverProvince;
    @Column(name = "receiver_city")
    private String receiverCity;
    @Column(name = "receiver_region")
    private String receiverRegion;
    @Column(name = "receiver_detailed")
    private String receiverDetailed;
    @Column(name = "send_name")
    private String sendName;
    @Column(name = "send_man_head")
    private String sendManHead;
    @Column(name = "send_phone")
    private String sendPhone;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_price")
    private BigDecimal productPrice;
    @Column(name = "product_cost")
    private BigDecimal productCost;
    @Column(name = "product_num")
    private int productNum;
    @Convert(converter = PayTypeConverter.class)
    @Column(name = "pay_way")
    private PayType payWay;
    @Column(name = "order_comment")
    private String orderComment;
    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date orderTime;
    @Column(name = "pay_cert_photo")
    private String payCertPhoto;
    @Column(name = "pay_cert_info")
    private String payCertInfo;
    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "recommend_phone")
    private String recommendPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "serviceOrderList",fetch = FetchType.LAZY)
    private Set<User> higherUserList = new TreeSet<>();
    @Formula("MONTH(order_time)")
    private String month;
    @Formula("datediff(now(),order_time)")
    private int diffDate;
    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @PrimaryKeyJoinColumn
    private WxPayOrderNotify wxPayOrderNotify;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @PrimaryKeyJoinColumn
    private Rebate rebate;

    public WxPayOrderNotify getWxPayOrderNotify() {
        return wxPayOrderNotify;
    }

    public void setWxPayOrderNotify(WxPayOrderNotify wxPayOrderNotify) {
        this.wxPayOrderNotify = wxPayOrderNotify;
    }

    public Rebate getRebate() {
        return rebate;
    }

    public void setRebate(Rebate rebate) {
        this.rebate = rebate;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public PayType getPayWay() {
        return payWay;
    }

    public void setPayWay(PayType payWay) {
        this.payWay = payWay;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductCost() {
        return productCost;
    }

    public void setProductCost(BigDecimal productCost) {
        this.productCost = productCost;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailed() {
        return receiverDetailed;
    }

    public void setReceiverDetailed(String receiverDetailed) {
        this.receiverDetailed = receiverDetailed;
    }

    public String getRecommendPhone() {
        return recommendPhone;
    }

    public void setRecommendPhone(String recommendPhone) {
        this.recommendPhone = recommendPhone;
    }

    public String getPayCertPhoto() {
        return payCertPhoto;
    }

    public void setPayCertPhoto(String payCertPhoto) {
        this.payCertPhoto = payCertPhoto;
    }

    public String getPayCertInfo() {
        return payCertInfo;
    }

    public void setPayCertInfo(String payCertInfo) {
        this.payCertInfo = payCertInfo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Set<User> getHigherUserList() {
        return higherUserList;
    }

    public void setHigherUserList(Set<User> higherUserList) {
        this.higherUserList = higherUserList;
    }

    public String getSendManHead() {
        return sendManHead;
    }

    public void setSendManHead(String sendManHead) {
        this.sendManHead = sendManHead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        return orderCode.equals(order.orderCode);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + orderCode.hashCode();
        return result;
    }
    public int getDiffDate() {
        return diffDate;
    }

    public void setDiffDate(int diffDate) {
        this.diffDate = diffDate;
    }

}
