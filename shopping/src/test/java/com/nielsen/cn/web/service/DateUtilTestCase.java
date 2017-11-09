package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.web.util.DateUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {ShoppingApplication.class})
public class DateUtilTestCase{

    @Test
    public void testGetBeginDateLastMonth() {
        Assert.assertEquals("2017-10",DateUtil.getLastMonth());
    }

}