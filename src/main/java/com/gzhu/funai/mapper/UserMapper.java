package com.gzhu.funai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhu.funai.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *@Author :wuxiaodong
 *@Date: 2023/7/16 16:45
 *@Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
