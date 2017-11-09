package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.web.enumerate.DirectorLevel;
import com.dlsw.cn.web.enumerate.RebateType;
import com.dlsw.cn.web.enumerate.RoleType;
import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.Rebate;
import com.dlsw.cn.web.po.User;
import com.dlsw.cn.web.repositories.OrderRepository;
import com.dlsw.cn.web.repositories.RebateRepository;
import com.dlsw.cn.web.repositories.UserRepository;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.RewardStrategyService;
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

    private final static int MAX_PERCENT = 36;

    @Override
    public void calRebate(Order order) {
        User orderUser = order.getUser();
        if(orderUser.getRoleType() == RoleType.高级合伙人){
            int curPercent = 0;
            User higherUser = userRepository.findByPhone(order.getRecommendPhone());
            while (higherUser != null && curPercent <= MAX_PERCENT) {
                int diff = DirectorLevel.getDirectorLevel(higherUser).getPercent() - DirectorLevel.getDirectorLevel(orderUser).getPercent();
                if (diff > 0) {
                    Rebate rebate = new Rebate();
                    rebate.setUser(higherUser);
                    rebate.setOrder(order);
                    rebate.setRebateType(RebateType.级差返利);
                    rebate.setReason(higherUser.getPhone() + "(" + higherUser.getNickname() + ")->" + DirectorLevel.getDirectorLevel(higherUser).getName() + ") - " + orderUser.getPhone() + "(" + orderUser.getNickname() + ")->" + DirectorLevel.getDirectorLevel(orderUser).getName());
                    rebate.setRebateTime(order.getOrderTime());
                    rebate.setRebate(order.getProductCost().multiply(new BigDecimal(diff).divide(new BigDecimal(100))));
                    curPercent += diff;
                    rebateRepository.save(rebate);
                } else {
                    if (curPercent == 0 && higherUser.getRoleType() == RoleType.高级合伙人 && order.getUser().getRoleType() == RoleType.高级合伙人
                            && DirectorLevel.getDirectorLevel(higherUser) == DirectorLevel.信 && DirectorLevel.getDirectorLevel(order.getUser()) == DirectorLevel.信) {
                        Rebate rebate = new Rebate();
                        rebate.setUser(higherUser);
                        rebate.setOrder(order);
                        rebate.setRebateType(RebateType.平级信返利);
                        rebate.setReason("平级信返利");
                        rebate.setRebateTime(order.getOrderTime());
                        rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.04)));
                        rebateRepository.save(rebate);
                        break;
                    }
                }
                higherUser = higherUser.getHigher();
                orderUser = higherUser;
            }
        }
    }
}
