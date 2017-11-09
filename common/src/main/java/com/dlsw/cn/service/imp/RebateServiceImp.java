package com.dlsw.cn.service.imp;

import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.RebateType;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.repositories.RebateRepository;
import com.dlsw.cn.repositories.UserRepository;
import com.dlsw.cn.service.BaseService;
import com.dlsw.cn.service.RebateService;
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
public class RebateServiceImp extends BaseService implements RebateService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RebateRepository rebateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    private final static int MAX_PERCENT = 24;

    @Override
    public void calSeniorRebate(User recommend, Order order) {
        if (order.getUser().getRoleType() == RoleType.高级合伙人 && recommend.getRoleType() == RoleType.高级合伙人) {
            Rebate rebate = new Rebate();
            rebate.setUser(recommend);
            rebate.setOrder(order);
            rebate.setReason("平级高级返利");
            rebate.setRebateType(RebateType.平级高级返利);
            rebate.setRebateTime(order.getOrderTime());
            rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.12)));
            rebateRepository.save(rebate);
        }
    }

    @Override
    public void calCreditRebate(User recommend, Order order) {
        if (DirectorLevel.getDirectorLevel(recommend) == DirectorLevel.信 && DirectorLevel.getDirectorLevel(order.getUser()) == DirectorLevel.信) {
            Rebate rebate = new Rebate();
            rebate.setUser(recommend);
            rebate.setOrder(order);
            rebate.setRebateType(RebateType.平级信返利);
            rebate.setReason("平级信返利");
            rebate.setRebateTime(order.getOrderTime());
            rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.04)));
            rebateRepository.save(rebate);
        }
    }

    @Override
    public void calLevelRebate(User recommend, Order order) {
        int curPercent = 0;
        User lowerUser = order.getUser();
        User higherUser = recommend;
        while (recommend != null && curPercent != MAX_PERCENT) {
            int diff = DirectorLevel.getDirectorLevel(higherUser).getPercent() - DirectorLevel.getDirectorLevel(lowerUser).getPercent();
            if (diff > 0) {
                Rebate rebate = new Rebate();
                rebate.setUser(recommend);
                rebate.setOrder(order);
                rebate.setRebateType(RebateType.级差返利);
                rebate.setReason("级差返利");
                rebate.setRebateTime(order.getOrderTime());
                rebate.setRebate(order.getProductCost().multiply(new BigDecimal(diff).divide(new BigDecimal(100))));
                rebateRepository.save(rebate);
            }
            curPercent += diff;
            higherUser = higherUser.getHigher();
            lowerUser = higherUser;
        }
    }

    @Override
    public void calRebate(Order order) {
        User recommend = userRepository.findByPhone(order.getRecommendPhone());
        calSeniorRebate(recommend, order);
        calCreditRebate(recommend, order);
        calLevelRebate(recommend, order);
    }
}
