package com.dlsw.cn.common.service.imp;

import com.dlsw.cn.common.enumerate.DirectorLevel;
import com.dlsw.cn.common.enumerate.RebateType;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.Rebate;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.OrderRepository;
import com.dlsw.cn.common.repositories.RebateRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.BaseService;
import com.dlsw.cn.common.service.RewardStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
@Service
public class RewardStrategyServiceImp extends BaseService implements RewardStrategyService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RebateRepository rebateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    private final static int MAX_PERCENT = DirectorLevel.信.getPercent();

    @Override
    public void calRebate(Order order) {
        User orderUser = order.getUser();
        if (orderUser.getRoleType() == RoleType.高级合伙人) {
            if (DirectorLevel.getDirectorLevel(orderUser) == DirectorLevel.信) {
                User higherUser = userRepository.findByPhone(order.getRecommendPhone());
                while (higherUser != null) {
                    if (higherUser.getRoleType() == RoleType.高级合伙人 && DirectorLevel.getDirectorLevel(higherUser) == DirectorLevel.信 && higherUser.getLevel() > 0) {
                        Rebate rebate = new Rebate();
                        rebate.setUser(higherUser);
                        rebate.setOrder(order);
                        rebate.setRebateType(RebateType.信平级返利);
                        rebate.setReason("信平级返利");
                        rebate.setRebateTime(order.getOrderTime());
                        rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.04)));
                        rebateRepository.save(rebate);
                        return;
                    }
                    higherUser = higherUser.getHigher();
                }
            } else {
                int curPercent = 0;
                User higherUser = userRepository.findByPhone(order.getRecommendPhone());
                while (higherUser != null) {
                    if (curPercent >= MAX_PERCENT || higherUser.getLevel() == 0) {
                        return;
                    }
                    int diff = DirectorLevel.getDirectorLevel(higherUser).getPercent() - DirectorLevel.getDirectorLevel(orderUser).getPercent();
                    if (diff > 0) {
                        Rebate rebate = new Rebate();
                        rebate.setUser(higherUser);
                        rebate.setOrder(order);
                        rebate.setRebateType(RebateType.级差返利);
                        rebate.setReason(higherUser.getPhone() + "(" + higherUser.getNickname() + "-" + DirectorLevel.getDirectorLevel(higherUser).getName() + ") -> " + orderUser.getPhone() + "(" + orderUser.getNickname() + "-" + DirectorLevel.getDirectorLevel(orderUser).getName() + ")");
                        rebate.setRebateTime(order.getOrderTime());
                        rebate.setRebate(order.getProductCost().multiply(new BigDecimal(diff).divide(new BigDecimal(100))));
                        curPercent += diff;
                        rebateRepository.save(rebate);
                    }
                    higherUser = higherUser.getHigher();
                    orderUser = higherUser;
                }
            }

        }
    }
}
