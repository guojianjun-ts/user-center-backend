package com.gjj.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gjj.usercenter.model.domain.request.usersLoginRequest;
import com.gjj.usercenter.model.domain.request.usersRegisterRequest;
import com.gjj.usercenter.model.domain.users;
import com.gjj.usercenter.service.usersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gjj.usercenter.constant.usersConstant.ADMIN_ROLE;
import static com.gjj.usercenter.constant.usersConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author gjj
 */

@RestController
@RequestMapping("/user")
public class usersController {
    @Resource
    private usersService userService;

    /**
     * 用户注册
     *
     * @return
     * @parm userRegisterRequest
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody usersRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @return
     * @parm userRegisterRequest
     */
    @PostMapping("/login")
    public users userLogin(@RequestBody usersLoginRequest userLoginRequest, HttpServletRequest request) {


        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @PostMapping("/logout")
    public Integer userLogin(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return userService.userLogOut(request);
    }


    @GetMapping("/current")
    public users getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        users currentUser = (users) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId=currentUser.getId();
        //todo 检验用户是否合法
        users user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @GetMapping("/search")
    public List<users> userSearch(String userName, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<users> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("username", userName);
        }
        List<users> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean userDelete(@RequestBody long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        users user = (users) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
