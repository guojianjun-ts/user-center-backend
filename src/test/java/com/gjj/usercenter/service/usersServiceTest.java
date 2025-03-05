package com.gjj.usercenter.service;

import com.gjj.usercenter.model.domain.users;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 *
 * @author gjj
 */
@SpringBootTest
class usersServiceTest {

    @Resource
    private usersService usersService;

    @Test
    public void testAddUser() {
        users user=new users();
        user.setUsername("BaZaHey");
        user.setUserAccount("root");
        user.setAvatarUrl("\"C:\\users\\78568\\Pictures\\头像.jpg\"");
        user.setGender(0);
        user.setUserPassword("123456789");
        user.setPhone("123");
        user.setEmail("123");
        user.setUserRole(1);
        boolean result=usersService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }
}