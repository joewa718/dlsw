package com.dlsw.cn.web.service;

import com.dlsw.cn.web.po.User;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseService {

    protected String getEqualStr(User user) {
        if (StringUtils.isBlank(user.getOrgPath())) {
            return '>' + String.valueOf(user.getId()) + ">";
        } else {
            return user.getOrgPath() + String.valueOf(user.getId()) + ">";
        }
    }

    protected String bindOffSpringOrgPath(User higher, User lower) {
        return getOrgPath(higher) + (!StringUtils.isBlank(lower.getOrgPath()) ? lower.getOrgPath() : ">");
    }

    private String getOrgPath(User user) {
        if (!StringUtils.isBlank(user.getOrgPath())) {
            return user.getOrgPath() + user.getId();
        } else {
            return '>' + String.valueOf(user.getId());
        }
    }
}
