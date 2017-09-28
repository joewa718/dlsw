package com.dlsw.cn.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.web.dto.EntryDTO;
import com.dlsw.cn.web.service.ReportService;
import com.dlsw.cn.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ShoppingApplication.class)
public class ReportServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @BeforeMethod()
    public void setUp() throws Exception {
        System.out.println("-------start test user search------------");
    }

    @Test
    public void processMemberDistributionTest() {
        Map<String, Long> map = reportService.analysisMemberDistribution("18930983718");
        System.out.print(map);
    }

    @Test
    public void analysisOrderVolumeTest() {
        Map<String,List<EntryDTO<String,Long>>> map = reportService.analysisOrderVolume("18930983718");
        System.out.print(map);
    }

    @Test
    public void updateUserRoleTypeTest() {
       /* userService.upgradeUerRoleType();*/
    }
}