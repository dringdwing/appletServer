package com.vector.server.controller;


import com.vector.server.config.shiro.JwtUtil;
import com.vector.server.domain.vo.RegisterVo;
import com.vector.server.service.UserService;
import com.vector.server.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2022-12-04 07:00:15
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${applet.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public R register(@Valid @RequestBody RegisterVo registerVo) {
        int id = userService.registerUser(registerVo.getRegisterCode(), registerVo.getCode(), registerVo.getNickname(), registerVo.getPhoto());
        String token = jwtUtil.createToken(id);
        Set<String> permissions = userService.searchUserPermissions(id);
        saveCacheToken(token, id);

        return R.ok("用户注册成功").put("token", token).put("permissions", permissions);
    }


    private void saveCacheToken(String token, int userId) {
        redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
    }
}

