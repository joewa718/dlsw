package com.nielsen.cn.web.service;

import com.dlsw.cn.ShoppingApplication;
import com.dlsw.cn.common.enumerate.DirectorLevel;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.OrderRepository;
import com.dlsw.cn.common.repositories.ProductRepository;
import com.dlsw.cn.shopping.service.OrderCheckService;
import com.dlsw.cn.shopping.service.imp.OrderServiceImp;
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
public class DirectorLevelTest extends  BaseTestCase{
    @InjectMocks
    private OrderServiceImp orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderCheckService orderCheckService;

    @Test
    public void testGetDirectorLevel() {
        User user = mock(User.class);
        when(user.getLower()).thenReturn(addLowerUser(1));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.无);
        when(user.getLower()).thenReturn(addLowerUser(2));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.仁);
        when(user.getLower()).thenReturn(addLowerUser(4));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.义);
        when(user.getLower()).thenReturn(addLowerUser(6));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.礼);
        when(user.getLower()).thenReturn(addLowerUser(8));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.智);
        when(user.getLower()).thenReturn(addLowerUser(10));
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user), DirectorLevel.信);
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
    public void testSureOrderCase(){
        orderService.sureOrder("",123);
    }
}