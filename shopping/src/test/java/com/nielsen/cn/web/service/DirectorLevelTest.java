package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.User;
import com.dlsw.cn.web.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShoppingApplication.class)
@PowerMockIgnore({"javax.management.*","org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
public class DirectorLevelTest{
    @Autowired
    OrderService orderService;
    @Test
    public void testGetDirectorLevel() {
        User user = mock(User.class);
        when(user.getLower()).thenReturn(addLowerUser(1));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.无);
        when(user.getLower()).thenReturn(addLowerUser(2));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.仁);
        when(user.getLower()).thenReturn(addLowerUser(4));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.义);
        when(user.getLower()).thenReturn(addLowerUser(6));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.礼);
        when(user.getLower()).thenReturn(addLowerUser(8));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.智);
        when(user.getLower()).thenReturn(addLowerUser(10));
        org.junit.Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.信);
    }

    private Set<User> addLowerUser(int count) {
        Set<User> userSet = new HashSet<>();
        for (int i = 0; i < count; i++) {
            User user1 = new User();
            user1.setId(i);
            user1.setPhone("123");
            user1.setRoleType(RoleType.高级合伙人);
            userSet.add(user1);
        }
        return userSet;
    }

    @Test
    @PrepareForTest(Source.class)
    public void testCallInternalInstance() throws Exception {
        File file = mock(File.class);
        Source underTest = new Source();
        whenNew(File.class).withArguments("bbb").thenReturn(file);
        when(file.exists()).thenReturn(true);
        boolean flag =underTest.callInternalInstance("bbb");
        org.junit.Assert.assertEquals(flag,true);
    }


}