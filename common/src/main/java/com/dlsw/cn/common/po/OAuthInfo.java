package com.dlsw.cn.common.po;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "refreshToken", nullable = true)
    private String refreshToken;
    @Column(name = "accessToken", nullable = true)
    private String accessToken;
    @Column(name = "expires_in", nullable = true)
    private int expiresIn = -1;
    @Column(name = "open_id", nullable = true)
    private String openId;
    @Column(name = "scope", nullable = true)
    private String scope;
    @Column(name = "union_id", nullable = true)
    private String unionId;
    @ManyToOne
    @JoinColumn(name = "user_id")
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
