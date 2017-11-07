package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.repositories.ProductRepository;
import com.dlsw.cn.util.DateUtil;
import com.dlsw.cn.web.service.OrderCheckService;
import com.dlsw.cn.web.service.imp.OrderServiceImp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShoppingApplication.class})
public class DateUtilTestCase{

    @Test
    public void testGetBeginDateLastMonth() {
        Assert.assertEquals("2017-10",DateUtil.getLastMonth());
    }

}