package com.gzhu.funai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhu.funai.dto.UserRegisterRequest;
import com.gzhu.funai.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhu.funai.enums.LoginType;
import com.gzhu.funai.session.LoginSession;

import java.util.Map;

/**
 *@Author :wuxiaodong
 *@Date: 2023/7/16 16:48
 *@Description:
 */
public interface UserService{
    /**
     * 注册
     * @param userRegisterVo
     * @return
     */
    UserEntity register(UserRegisterRequest userRegisterVo);

    /**
     * 登录
     * @param loginSession
     * @return
     */
    Map<String,Object> login(LoginSession loginSession);

}
