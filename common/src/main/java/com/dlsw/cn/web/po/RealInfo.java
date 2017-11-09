package com.dlsw.cn.web.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@Table(name = "t_real_info")
public class RealInfo extends BaseEntity implements Serializable {
    @Id
    @Column(name="user_id", unique=true, nullable=false)
    @GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign", parameters=@org.hibernate.annotations.Parameter(name="property", value="user"))
    private long id;
    @Column(name = "idCard")
    private String idCard;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "sex")
    private String sex;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "idCard_photo_front")
    private String idCardPhotoFront;
    @Column(name = "idCard_photo_back")
    private String idCardPhotoBack;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "region")
    private String region;
    @Column(name = "is_audited")
    private boolean isAudited;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIdCardPhotoFront() {
        return idCardPhotoFront;
    }

    public void setIdCardPhotoFront(String idCardPhotoFront) {
        this.idCardPhotoFront = idCardPhotoFront;
    }

    public String getIdCardPhotoBack() {
        return idCardPhotoBack;
    }

    public void setIdCardPhotoBack(String idCardPhotoBack) {
        this.idCardPhotoBack = idCardPhotoBack;
    }

    public boolean isAudited() {
        return isAudited;
    }

    public void setAudited(boolean audited) {
        isAudited = audited;
    }

    public String getIdCard() {
        return idCard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}