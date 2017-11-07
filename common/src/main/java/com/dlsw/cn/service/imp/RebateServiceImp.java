package com.dlsw.cn.service.imp;

import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.OrderStatus;
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
import com.dlsw.cn.util.DateUtil;
import org.apache.poi.util.BinaryTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhanwang
 * @create 2017-11-06 13:16
 **/
@Service
public class RebateServiceImp extends BaseService implements RebateService {

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
            rebate.setReason("高级平级返利");
            rebate.setRebateType(RebateType.平级高级返利);
            rebate.setRebateTime(order.getOrderTime());
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
            rebate.setRebateType(RebateType.平级信返利);
            rebate.setReason("信平级返利");
            rebate.setRebateTime(order.getOrderTime());
            rebate.setRebate(order.getProductCost().multiply(new BigDecimal(0.04)));
            rebateRepository.save(rebate);
        }
    }

    public void calTeamRebate() {
        List<User> rebateUserList = new ArrayList<>();
        List<User> userList = userRepository.findByEqualsRoleType(RoleType.高级合伙人);
        userList.forEach(root -> {
            if (root == null) return;
            LinkedList<User> queue = new LinkedList<>();
            queue.offer(root);
            while (queue.size() > 0) {
                User node = queue.poll();
                rebateUserList.add(node);
                node.getLower().forEach(c_node -> {
                    if(DirectorLevel.getDirectorLevel(root).getCode() - DirectorLevel.getDirectorLevel(c_node).getCode() > 0){
                        queue.offer(c_node);
                    }
                });
            }
        });

    }
}
