package com.dlsw.cn.po;

import com.dlsw.cn.converter.RoleTypeConverter;
import com.dlsw.cn.enumerate.RoleType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "t_user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class User implements Serializable,Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
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
    @Column(name = "is_we_user")
    private Boolean isWeUser = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RealInfo> realInfo = new TreeSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OAuthInfo> oAuthInfo = new TreeSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DeliveryAddress> deliveryAddressList = new TreeSet<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private Set<Order> orderList = new TreeSet<>();;
    @OneToMany(mappedBy = "higher", cascade = { CascadeType.REFRESH, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    private Set<User> lower = new TreeSet<>();
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.PERSIST },fetch = FetchType.LAZY)
    @JoinColumn(name = "h_uid")
    private User higher;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "t_user_service_order",joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "order_id")})
    @OrderBy("id DESC")
    private Set<Order> serviceOrderList = new TreeSet<>();
    @Column(name = "org_path")
    private String orgPath;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "isVerificationPhone")
    private Boolean isVerificationPhone = false;
    @Column(name = "wx_password")
    private String wxPassword;
    @Column(name = "isWxLogin")
    private Boolean is_wxLogin;
    @Formula("datediff(now(),reg_time)")
    private int diffDate;
    @Column(name = "level")
    private Integer level;
    @OneToMany(mappedBy = "user", cascade = { CascadeType.REFRESH, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    private Set<Rebate> rebateSet = new TreeSet<>();

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isWeUser() {
        return isWeUser;
    }

    public void setWeUser(boolean weUser) {
        isWeUser = weUser;
    }

    public Boolean getWeUser() {
        return isWeUser;
    }

    public void setWeUser(Boolean weUser) {
        isWeUser = weUser;
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

    public String getWxPassword() {
        return wxPassword;
    }

    public void setWxPassword(String wxPassword) {
        this.wxPassword = wxPassword;
    }

    public Boolean getIs_wxLogin() {
        return is_wxLogin;
    }

    public void setIs_wxLogin(Boolean is_wxLogin) {
        this.is_wxLogin = is_wxLogin;
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
        return realInfo.stream().findFirst().get();
    }

    public void setRealInfo(RealInfo realInfo) {
        this.realInfo.add(realInfo);
    }

    public OAuthInfo getOAuthInfo() {
        return oAuthInfo.stream().findFirst().get();
    }

    public void setOAuthInfo(OAuthInfo oAuthInfo) {
        this.oAuthInfo.add(oAuthInfo);
    }

}
