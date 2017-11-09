package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.web.enumerate.RoleType;
import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.PromoteLog;
import com.dlsw.cn.web.po.User;
import com.dlsw.cn.web.repositories.PromoteLogRepository;
import com.dlsw.cn.web.repositories.UserRepository;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.PromoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author zhanwang
 * @create 2017-10-28 14:07
 **/
@Service
public class PromoteServiceImp extends BaseService implements PromoteService {
    private final Logger logger = LoggerFactory.getLogger(PromoteServiceImp.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PromoteLogRepository promoteLogRepository;

    /**
     * 监控VIP董事,满足3+6升级合伙人
     */
    @Override
    @Transactional
    public void monitorVip(Order order) {
        User orderUser = order.getUser();
        User higher = orderUser.getHigher();
        while (higher != null) {
            if (higher.getRoleType() == RoleType.VIP) {
                long oneLevel = userRepository.findSumByOneLevelOrgPath(getEqualStr(higher), RoleType.VIP);
                if (oneLevel >= 3) {
                    long otherLevel = userRepository.findSumByLikeOrgPath(getEqualStr(higher), RoleType.VIP);
                    if (otherLevel >= 9) {
                        PromoteLog promoteLog = savePromoteLog(higher, RoleType.合伙人);
                        promoteLogRepository.save(promoteLog);
                        higher.setRoleType(RoleType.合伙人);
                        userRepository.save(higher);
                        logger.debug("-代理商:" + higher.getNickname() + "(" + higher.getPhone() + ") ,直属VIP(" + oneLevel + "),团队VIP(" + otherLevel + "),因此升级到合伙人-");
                    }
                }
            }
            higher = orderUser.getHigher();
        }
    }

    /**
     * 监控合伙人董事,满足3+9升级高级合伙人
     */
    @Override
    public void monitorPartner(Order order) {
        User orderUser = order.getUser();
        User higher = orderUser.getHigher();
        while (higher != null) {
            if (higher.getRoleType() == RoleType.合伙人) {
                long oneLevel = userRepository.findSumByOneLevelOrgPath(getEqualStr(higher), RoleType.合伙人);
                if (oneLevel >= 3) {
                    long otherLevel = userRepository.findSumByLikeOrgPath(getEqualStr(higher), RoleType.合伙人);
                    if (otherLevel >= 12) {
                        PromoteLog promoteLog = savePromoteLog(higher, RoleType.高级合伙人);
                        promoteLogRepository.save(promoteLog);
                        higher.setRoleType(RoleType.高级合伙人);
                        userRepository.save(higher);
                        logger.debug("-代理商:" + higher.getNickname() + "(" + higher.getPhone() + ") ,直属VIP(" + oneLevel + "),团队VIP(" + otherLevel + "),因此升级到合伙人-");
                    }
                }
            }
            higher = orderUser.getHigher();
        }
    }

    private PromoteLog savePromoteLog(User higher, RoleType roleType) {
        PromoteLog promoteLog = new PromoteLog();
        promoteLog.setUser(higher);
        promoteLog.setFormRoleType(higher.getRoleType());
        promoteLog.setToRoleType(roleType);
        promoteLog.setUpdate_time(new Date());
        return promoteLog;
    }
}
