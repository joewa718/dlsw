package com.dlsw.cn.common.po;

import com.dlsw.cn.common.converter.RoleTypeConverter;
import com.dlsw.cn.common.enumerate.RoleType;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "t_user")
public class User extends BaseEntity implements Serializable, Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "head_Portrait")
    private String headPortrait;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "score")
    private BigDecimal score = BigDecimal.valueOf(0);
    @Column(name = "disable")
    private Boolean disable = false;
    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "role_type")
    private RoleType roleType;
    @Column(name = "authorization_code")
    private String authorizationCode;
    @Column(name = "isReceiveMessage")
    private Boolean isReceiveMessage = false;
    @Column(name = "reg_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regTime;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RealInfo> realInfo;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OAuthInfo> oAuthInfo;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DeliveryAddress> deliveryAddressList = new TreeSet<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private Set<Order> orderList = new TreeSet<>();
    @OneToMany(mappedBy = "higher", cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<User> lower = new TreeSet<>();
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "h_uid")
    private User higher;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "t_user_service_order", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "order_id")})
    @OrderBy("id DESC")
    private Set<Order> serviceOrderList = new TreeSet<>();
    @Column(name = "org_path")
    private String orgPath;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "isVerificationPhone")
    private Boolean isVerificationPhone = false;
    @Formula("datediff(now(),reg_time)")
    private int diffDate;
    @Column(name = "level")
    private Integer level;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<Rebate> rebateSet = new TreeSet<>();
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<PromoteLog> promoteLogSet = new TreeSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Boolean getReceiveMessage() {
        return isReceiveMessage;
    }

    public void setReceiveMessage(Boolean receiveMessage) {
        isReceiveMessage = receiveMessage;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Set<DeliveryAddress> getDeliveryAddressList() {
        return deliveryAddressList;
    }

    public void setDeliveryAddressList(Set<DeliveryAddress> deliveryAddressList) {
        this.deliveryAddressList = deliveryAddressList;
    }

    public Set<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(Set<Order> orderList) {
        this.orderList = orderList;
    }

    public Set<Order> getServiceOrderList() {
        return serviceOrderList;
    }

    public void setServiceOrderList(Set<Order> serviceOrderList) {
        this.serviceOrderList = serviceOrderList;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return phone.equals(user.phone);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + phone.hashCode();
        return result;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public Set<User> getLower() {
        return lower;
    }

    public void setLower(Set<User> lower) {
        this.lower = lower;
    }

    public User getHigher() {
        return higher;
    }

    public void setHigher(User higher) {
        this.higher = higher;
    }

    public int getDiffDate() {
        return diffDate;
    }

    public void setDiffDate(int diffDate) {
        this.diffDate = diffDate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean isVerificationPhone() {
        return isVerificationPhone;
    }

    public void setVerificationPhone(Boolean verificationPhone) {
        isVerificationPhone = verificationPhone;
    }

    public Boolean getVerificationPhone() {
        return isVerificationPhone;
    }

    public Set<Rebate> getRebateSet() {
        return rebateSet;
    }

    public void setRebateSet(Set<Rebate> rebateSet) {
        this.rebateSet = rebateSet;
    }

    public RealInfo getRealInfo() {
        return realInfo.size() > 0 ? realInfo.get(0) : null;
    }

    public void setRealInfo(RealInfo realInfo) {
        this.realInfo.clear();
        this.realInfo.add(realInfo);
    }

    public OAuthInfo getOAuthInfo() {
        return oAuthInfo.size() > 0 ? oAuthInfo.get(0) : null;
    }

    public void setOAuthInfo(OAuthInfo oAuthInfo) {
        this.oAuthInfo.clear();
        this.oAuthInfo.add(oAuthInfo);
    }

    public Set<PromoteLog> getPromoteLogSet() {
        return promoteLogSet;
    }

    public void setPromoteLogSet(Set<PromoteLog> promoteLogSet) {
        this.promoteLogSet = promoteLogSet;
    }

}
