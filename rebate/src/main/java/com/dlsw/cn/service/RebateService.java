package com.dlsw.cn.service;

import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanwang
 * @create 2017-10-28 14:07
 **/
@Service
public class RebateService implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(RebateService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * 监控VIP董事,满足3+6升级合伙人
     */
    public void monitorVip(){
        logger.debug("-开始执行VIP升级任务-");
        List<User> vipList= userRepository.findByEqualsRoleType(RoleType.高级合伙人);
        vipList.forEach(user -> {
            long oneLevel = userRepository.findSumByOneLevelOrgPath(user.getOrgPath(),RoleType.VIP);
            if(oneLevel >= 3){
                long otherLevel = userRepository.findSumByLikeOrgPath(user.getOrgPath(),RoleType.VIP);
                if(otherLevel >= 9){
                    user.setRoleType(RoleType.合伙人);
                    userRepository.save(user);
                    logger.debug("-代理商:"+user.getNickname()+"("+user.getPhone()+") ,直属VIP("+oneLevel+"),团队VIP("+otherLevel+"),因此升级到合伙人-");
                }
            }
        });
        logger.debug("-结束执行VIP升级任务-");
    }
    /**
     * 监控合伙人董事,满足3+9升级高级合伙人
     */
    public void monitorPartner(){
        logger.debug("-开始执行合伙人升级任务-");
        List<User> vipList= userRepository.findByEqualsRoleType(RoleType.高级合伙人);
        vipList.forEach(user -> {
            long oneLevel = userRepository.findSumByOneLevelOrgPath(user.getOrgPath(),RoleType.高级合伙人);
            if(oneLevel >= 3){
                long otherLevel = userRepository.findSumByLikeOrgPath(user.getOrgPath(),RoleType.高级合伙人);
                if(otherLevel >= 12){
                    user.setRoleType(RoleType.合伙人);
                    userRepository.save(user);
                    logger.debug("-代理商:"+user.getNickname()+"("+user.getPhone()+") ,直属合伙人("+oneLevel+"),团队合伙人("+otherLevel+"),因此升级到高级合伙人-");
                }
            }
        });
        logger.debug("-结束执行合伙人升级任务-");
    }

    @Override
    public void run(String... strings) throws Exception {
        monitorVip();
        monitorPartner();
    }
}
