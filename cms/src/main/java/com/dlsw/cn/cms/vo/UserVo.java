package com.dlsw.cn.cms.vo;/**
 * Created by zhanwa01 on 2017/8/9.
 */

/**
 * @author zhanwang
 * @create 2017-08-09 11:18
 **/
public class UserVo {
    private long userId;
    private String phone;
    private String regCode;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }
}
