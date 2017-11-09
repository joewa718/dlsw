package com.dlsw.cn.po;

import com.dlsw.cn.converter.RoleTypeConverter;
import com.dlsw.cn.enumerate.RoleType;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhanwang
 * @create 2017-11-08 17:19
 **/
@Entity
@Table(name = "t_promote_log")
public class PromoteLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "from_role_type")
    private RoleType formRoleType;
    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "to_role_type")
    private RoleType toRoleType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date update_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleType getFormRoleType() {
        return formRoleType;
    }

    public void setFormRoleType(RoleType formRoleType) {
        this.formRoleType = formRoleType;
    }

    public RoleType getToRoleType() {
        return toRoleType;
    }

    public void setToRoleType(RoleType toRoleType) {
        this.toRoleType = toRoleType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
