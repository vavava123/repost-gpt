package com.gzhu.funai.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.enums.UserLevel;

import java.util.List;

/**
 * @author zhangxiaobing1
 * @data 2023/7/21
 */
public interface UserDao  extends IService<UserEntity> {
    Integer selectCountByMobile(String mobile);

    Integer selectCountByUsername(String userName);

    UserEntity getByLevelAndUsernameAndMobile(UserLevel userLevel, String userName,String mobile);

    UserEntity getByLevelAndUsername(UserLevel userLevel, String userName);

}
