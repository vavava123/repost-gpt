package com.gzhu.funai.handler.impl;

import com.gzhu.funai.dao.UserDao;
import com.gzhu.funai.dao.UserLoginRecordDao;
import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.entity.UserLoginRecord;
import com.gzhu.funai.enums.UserLevel;
import com.gzhu.funai.handler.LoginHandler;
import com.gzhu.funai.session.LoginSession;
import com.gzhu.funai.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangxiaobing1
 * @Date: 2023/4/30 21:09
 */

@Service(value = "NormalLoginHandler")
public class NormalLoginHandler implements LoginHandler {

    private static final String MAP_KEY_USERNAME = "username";

    private static final String MAP_KEY_MOBILE = "mobile";

    @Resource
    private UserDao userDao;

    @Resource
    private UserLoginRecordDao userLoginRecordDao;

    @Override
    public Map<String, Object> login(LoginSession loginSession) {
        Map<String, Object> map = new HashMap<>(5);

        // 去数据库查询 SELECT * FROM user WHERE level != ? and (username = ? OR mobile = ?)
        UserEntity userEntity = userDao.getByLevelAndUsernameAndMobile(UserLevel.VISITOR,
                loginSession.getLoginAcct(), loginSession.getMobile());

        // 无此账户，登录失败
        if (userEntity == null) {
            return null;
        }

        // 检验账户锁定情况
        if (userEntity.getStatus() == 1) {
            return map;
        }

        // 进行密码匹配失败
        if (!new BCryptPasswordEncoder().matches(loginSession.getPassword(), userEntity.getPassword())) {
            return null;
        }

        //登录成功，返回数据
        if (!StringUtils.isEmpty(userEntity.getNickname())) {
            map.put(MAP_KEY_USERNAME, userEntity.getNickname());
        } else if (!StringUtils.isEmpty(userEntity.getUsername())) {
            map.put(MAP_KEY_USERNAME, userEntity.getUsername());
        } else {
            map.put(MAP_KEY_USERNAME, userEntity.getMobile());
        }
        map.put("userId", userEntity.getId());
        map.put("userLevel", String.valueOf(userEntity.getLevel()));
        map.put("token", JwtUtil.createToken(userEntity.getId(), (String) (map.get(MAP_KEY_USERNAME)), userEntity.getLevel()));

        // 登录入库

        userLoginRecordDao.save(UserLoginRecord.builder()
                .userId(userEntity.getId())
                .loginType(loginSession.getLoginType().typeNo)
                .loginIp(loginSession.getIp())
                .build()
        );
        return map;
    }
}
