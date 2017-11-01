package com.dlsw;

import com.dlsw.cn.enumerate.DirectorLevel;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.User;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("unitTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DirectorLevelTest{


    @Test()
    public void testConvertLanguage() {
        User user = new User();
        Set<User> userSet=new HashSet();
        user.setLower(userSet);
        addLowerUser(userSet,1);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.无);
        userSet.clear();
        addLowerUser(userSet,3);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.仁);
        userSet.clear();
        addLowerUser(userSet,5);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.义);
        userSet.clear();
        addLowerUser(userSet,7);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.礼);
        userSet.clear();
        addLowerUser(userSet,9);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.智);
        userSet.clear();
        addLowerUser(userSet,11);
        Assert.assertEquals(DirectorLevel.getDirectorLevel(user),DirectorLevel.信);
    }

    private void addLowerUser(Set<User> userSet,int count) {
        for(int i =0 ;i<count;i++){
            User user1= new User();
            user1.setId(i);
            user1.setPhone("123");
            user1.setRoleType(RoleType.高级合伙人);
            userSet.add(user1);;
        }
    }

}