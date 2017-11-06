package com.dlsw.cn.service.imp;

import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.OrderStatus;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.repositories.RebateRepository;
import com.dlsw.cn.repositories.UserRepository;
import com.dlsw.cn.service.BaseService;
import com.dlsw.cn.service.RebateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
@Service
public class RebateServiceImp extends BaseService implements RebateService{

    @Autowired
    RebateRepository rebateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public void calSeniorRebate(User user, Order order) {
        if (order.getUser().getRoleType() == RoleType.高级合伙人 && user.getRoleType() == RoleType.高级合伙人) {
            Rebate rebate = new Rebate();
            rebate.setUser(user);
            rebate.setOrder(order);
            rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.12)));
            rebateRepository.save(rebate);
        }
    }

    @Override
    public void calCreditRebate(User user, Order order) {
        if (DirectorLevel.getDirectorLevel(user) == DirectorLevel.信 && DirectorLevel.getDirectorLevel(order.getUser()) == DirectorLevel.信) {
            Rebate rebate = new Rebate();
            rebate.setUser(user);
            rebate.setOrder(order);
            rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.04)));
            rebateRepository.save(rebate);
        }
    }

    public void calTeamRebate(){
        List<User> userList = userRepository.findByEqualsRoleType(RoleType.高级合伙人);
        Map<User, BigDecimal> doubleMap =new HashMap<>();
        userList.forEach(user ->{
            List<Object[]> list = orderRepository.findOrderListByOrderTime(getLikeStr(user), OrderStatus.已支付,null,null);
            list.forEach(objects -> {
                User lowUser = (User) objects[0];
                Double cost = (Double) objects[1];
                int differ = DirectorLevel.getDirectorLevel(user).getCode() - DirectorLevel.getDirectorLevel(lowUser).getCode();
                doubleMap.put(lowUser,new BigDecimal(cost).multiply(new BigDecimal(differ)));
            });
        });
    }
}
