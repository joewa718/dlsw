package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.enumerate.OrderStatus;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-11-06 16:20
 **/
@SpringBootTest(classes = {ShoppingApplication.class})
public class OrderRepositoryTestCase extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    OrderRepository orderRepository;

    @Test(dataProvider = "orderList")
    public void testFindOrderListByOrderTimeCase(List<Order> orderList) {
        List<Object[]> list = orderRepository.findOrderListByOrderTime(">5>", OrderStatus.已支付, new Date(), new Date());
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
