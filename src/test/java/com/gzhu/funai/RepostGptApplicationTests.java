package com.gzhu.funai;

import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RepostGptApplicationTests {

        @Resource
        private UserMapper u;

    @Test
    void contextLoads() {
    }


    @Test
    public void h(){
        UserEntity userEntity = UserEntity.builder()
                .id("高压釜")
                .username("四大皆空发过火")
                .password("hello")
                .build();

        u.insert(userEntity);

    }


}
