package com.gzhu.funai.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhu.funai.dao.UserDao;
import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.enums.UserLevel;
import com.gzhu.funai.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangxiaobing1
 * @data 2023/7/21
 */
@Repository
public class UserDaoImpl extends ServiceImpl<UserMapper, UserEntity> implements UserDao {

    @Resource
    private UserMapper userMapper;

    @Override
    public Integer selectCountByMobile(String mobile) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getMobile, mobile);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer selectCountByUsername(String userName) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, userName);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public UserEntity getByLevelAndUsernameAndMobile(UserLevel userLevel, String userName, String mobile) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserEntity::getLevel, userLevel.levelNo);
        queryWrapper.and(q -> {
            q.eq(UserEntity::getUsername, userName).or()
                    .eq(UserEntity::getMobile, mobile);
        }).last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }


    @Override
    public UserEntity getByLevelAndUsername(UserLevel userLevel, String userName){
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserEntity::getLevel, userLevel.levelNo);
        queryWrapper.eq(UserEntity::getUsername, userName).last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }
}
