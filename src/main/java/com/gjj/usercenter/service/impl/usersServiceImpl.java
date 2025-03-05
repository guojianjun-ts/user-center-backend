package com.gjj.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.usercenter.model.domain.users;
import com.gjj.usercenter.service.usersService;
import com.gjj.usercenter.mapper.usersMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gjj.usercenter.constant.usersConstant.USER_LOGIN_STATE;

/**
 * @author 78568
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-03-02 15:33:40
 */
@Service
@Slf4j
public class usersServiceImpl extends ServiceImpl<usersMapper, users> implements usersService {

    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[\\p{P}\\p{S}\\s]");
    private static final String SALT = "gjj"; //防止别人偷走我的秘方，使密码更加混乱

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        //1. 输入字段非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

        //2. 账户长度不少于4位
        if (userAccount.length() < 4) {
            return -1;
        }

        //3. 密码长度不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        //4. 账户不能重复
        QueryWrapper<users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //5. 账户不包含特殊字符
        Matcher matcher = SPECIAL_CHAR_PATTERN.matcher(userAccount);
        if (matcher.find()) { // 如果找到特殊字符
            return -1;
        }

        //6. 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //7.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //8.插入数据
        users user = new users();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public users userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1. 校验输入字段
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        Matcher matcher = SPECIAL_CHAR_PATTERN.matcher(userAccount);
        if (matcher.find()) { // 如果找到特殊字符
            return null;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.询用户是否存在
        QueryWrapper<users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        users user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        //4. 用户脱敏
        users safetyUser = getSafetyUser(user);
        //5. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;

    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public users getSafetyUser(users originUser){
        if (originUser == null) {
            return null;
        }
        users safetyUser = new users();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserPassword(originUser.getUserPassword());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(0);
        safetyUser.setUserRole(+originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(new Date());
        safetyUser.setIsDelete(0);

        return safetyUser;
    }

}