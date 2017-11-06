package com.nielsen.cn.web.service;

import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTestCase extends AbstractTestNGSpringContextTests {

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}