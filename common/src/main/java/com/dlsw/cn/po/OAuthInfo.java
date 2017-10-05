package com.dlsw.cn.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhanwang
 * @create 2017-08-26 16:40
 **/
@Entity
@Table(name = "t_oauth_info")
public class OAuthInfo extends BaseEntity {
    @Id
    @GenericGenerator(name = "generator",
            strategy = "foreign",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "property", value = "user")
            })
    @GeneratedValue(generator = "generator")
    @Column(name = "ID")
    private long id;
    @Column(name = "refreshToken")
    private String refreshToken;
    @Column(name = "accessToken")
    private String accessToken;
    @Column(name = "expires_in")
    private int expiresIn = -1;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "scope")
    private String scope;
    @Column(name = "union_id")
    private String unionId;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    private User user;

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
