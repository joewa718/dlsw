package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.repositories.OrderRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.RewardStrategyService;
import com.dlsw.cn.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.util.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-11-06 16:20
 **/
@SpringBootTest(classes = {ShoppingApplication.class})
public class OrderRepositoryTestCase extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RewardStrategyService rewardStrategyService;
    @Test()
    public void testFindOrderListByOrderTimeCase() {
       List<Object[]> list = orderRepository.findOrderListByOrderTime(">5>", OrderStatus.已支付, DateUtil.getCurMonth());
        Assert.notNull(list,"aaa");
    }

    @DataProvider(name = "orderList")
    public static Object[][] dataProviderMethod() {
        List<Order> orderList = new ArrayList<Order>();
        Order order = new Order();
        order.setUser(null);
        orderList.add(order);
        return new Object[][]{
                {orderList}
        };
    }

}
