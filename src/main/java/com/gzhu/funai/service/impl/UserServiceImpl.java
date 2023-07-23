package com.gzhu.funai.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gzhu.funai.dao.UserDao;
import com.gzhu.funai.dto.UserRegisterRequest;
import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.enums.LoginType;
import com.gzhu.funai.exception.PhoneException;
import com.gzhu.funai.exception.UsernameException;
import com.gzhu.funai.handler.LoginHandler;
import com.gzhu.funai.handler.impl.NormalLoginHandler;
import com.gzhu.funai.handler.impl.VisitorLoginHandler;
import com.gzhu.funai.service.UserService;
import com.gzhu.funai.session.LoginSession;
import com.gzhu.funai.utils.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.EnumMap;
import java.util.Map;

/**
 *@Author :wuxiaodong
 *@Date: 2023/7/16 16:48
 *@Description:
 */
@Service(value = "UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private NormalLoginHandler normalLoginHandler;
    @Resource
    private VisitorLoginHandler visitorLoginHandler;

    private final Map<LoginType, LoginHandler> loginHandlerMap = new EnumMap<>(LoginType.class);

    /**
     * 使用@PostConstruct注解的方法需要在依赖注入完成之后执行。
     * 该注解只能用于被Spring管理的bean中的方法，且只执行一次。
     * 在方法完成后，bean就可以使用了。
     */
    @PostConstruct
    public void init() {
        loginHandlerMap.put(LoginType.NORMAL, normalLoginHandler);
        loginHandlerMap.put(LoginType.VISITOR, visitorLoginHandler);
    }

    @Override
    public UserEntity register(UserRegisterRequest req) {
        // 1、用户名、电话判重
        if (StringUtils.isNotBlank(req.getPhone())) {
            req.setPhone(req.getPhone().trim());
        }

        if (userDao.selectCountByMobile(req.getPhone()) > 0 ) {
            throw new PhoneException();
        }
        if (userDao.selectCountByUsername(req.getUsername()) > 0 ) {
            throw new UsernameException();
        }
        // 2、封装实体对象并持久化
        UserEntity userEntity = UserEntity.of(req);

        // 3、密码进行MD5盐值加密.不能直接对原密码加密，可能有网站收集了大量常见的MD5加密后的密码，暴力解法。
        // 加盐增加密码的复杂性再进行加密减小被破解的可能性。
        userEntity.setPassword(PasswordEncoderUtil.encoder.encode(req.getPassword()));

        userDao.save(userEntity);

        return userEntity;
    }

    @Override
    public Map<String,Object> login(LoginSession loginSession) {
        return loginHandlerMap.get(loginSession.getLoginType()).login(loginSession);
    }


}
