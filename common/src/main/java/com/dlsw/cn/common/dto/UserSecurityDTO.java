package com.dlsw.cn.common.dto;

import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.common.po.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserSecurityDTO extends org.springframework.security.core.userdetails.User {
    private long id;
    private String nickname;
    private String phone;
    private String email;
    private Boolean disable;
    private String clientName;
    private RoleType roleType;

    public UserSecurityDTO() {
        super(null, null, null);
    }

    public UserSecurityDTO(String phone, String password, Collection<? extends GrantedAuthority> authorities, User user)
            throws IllegalArgumentException {
        super(phone, password, authorities);
        if (user != null) {
            this.nickname = user.getNickname();
            this.phone = user.getPhone();
            this.id = user.getId();
            this.email = user.getEmail();
            this.disable = user.getDisable();
            this.roleType= user.getRoleType();
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getDisable() {
        return disable;
    }


    public void setDisable(Boolean disable) {
        this.disable = disable;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
