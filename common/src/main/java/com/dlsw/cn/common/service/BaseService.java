package com.dlsw.cn.common.service;

import com.dlsw.cn.common.po.User;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseService {

    protected String getOrgPath(User user) {
        if (StringUtils.isBlank(user.getOrgPath())) {
            return '>' + String.valueOf(user.getId()) + ">";
        } else {
            return user.getOrgPath() + String.valueOf(user.getId()) + ">";
        }
    }

    protected String bindOffSpringOrgPath(User higher, User lower) {
        String orgPath;
        if (!StringUtils.isBlank(higher.getOrgPath())) {
            orgPath = higher.getOrgPath() + higher.getId();
        } else {
            orgPath = '>' + String.valueOf(higher.getId());
        }
        return orgPath + (!StringUtils.isBlank(lower.getOrgPath()) ? lower.getOrgPath() : ">");
    }

}
