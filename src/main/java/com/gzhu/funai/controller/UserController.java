package com.gzhu.funai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhu.funai.dto.UserLoginRequest;
import com.gzhu.funai.dto.UserRegisterRequest;
import com.gzhu.funai.entity.UserAdvicesEntity;
import com.gzhu.funai.entity.UserApiKeyEntity;
import com.gzhu.funai.entity.UserEntity;
import com.gzhu.funai.enums.ApiType;
import com.gzhu.funai.enums.LoginType;
import com.gzhu.funai.enums.SessionType;
import com.gzhu.funai.enums.UserLevel;
import com.gzhu.funai.service.UserService;
import com.gzhu.funai.session.LoginSession;
import com.gzhu.funai.utils.ResultCode;
import com.gzhu.funai.utils.ReturnResult;
import com.gzhu.funai.utils.VerificationCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author :wuxiaodong
 * @Date: 2023/7/16 14:58
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    //    @Resource
    //    private StringRedisTemplate stringRedisTemplate;

    //    @Resource
    //    private SmsComponent smsComponent;
    //
    //    @Resource
    //    private ChatService chatService;
    //
    //    @Resource
    //    private UserSessionService userSessionService;
    //
    //    @Resource
    //    private UserApiKeyService userApiKeyService;
    //
    //    @Resource
    //    private UserAdvicesService userAdvicesService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ReturnResult register(@Valid @RequestBody UserRegisterRequest req, HttpServletRequest request) {

        //验证码通过，真正注册
        UserEntity register = userService.register(req);

        // 给新用户直接登录
        log.info("号码{}注册成功，已自动登录！", req.getPhone());
        return ReturnResult.ok().data(userService.login(
                LoginSession.builder()
                        .loginAcct(req.getUsername())
                        .password(req.getPassword())
                        .loginType(LoginType.NORMAL)
                        .ip(request.getRemoteAddr())
                        .build()
        ));
    }

    /**
     * 登录（账号登录 / 游客登录）
     */
    @PostMapping("/login")
    public ReturnResult login(@RequestBody @Valid UserLoginRequest req, HttpServletRequest request) {

        Map<String, Object> map = userService.login(
                LoginSession.builder()
                        .loginAcct(req.getLoginAcct())
                        .password(req.getPassword())
                        .loginType(LoginType.get(req.getLoginType()))
                        .ip(request.getRemoteAddr())
                        .build());

        if (map == null) {
            return ReturnResult.error().message("用户名或密码错误");
        } else if (map.isEmpty()) {
            return ReturnResult.error().message("账号已被锁定，请与管理员联系！！！");
        }
        //  登录成功，map包含用户名和token.要求前端将token串保留在cookie中，前端请求拦截器把cookie的token放在请求头的字段中
        return ReturnResult.ok().data(map);
    }


}
