package com.dlsw.cn.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.web.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShoppingApplication.class)
@WebAppConfiguration
public class OrderServiceTest {
    @Autowired
    WebApplicationContext webApplicationConnect;

    @Autowired
    private OrderService orderService;

    private MockMvc mvc;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();

    }

    @Test
    public void testLogin() throws Exception {
        String uri = "/api/user/login";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .param("username","13524998437")
                .param("password","123456")
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
    }
}
