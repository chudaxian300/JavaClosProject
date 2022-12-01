package com.zgnba.clos.controller;

import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.JwtUtil;
import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.form.req.UserLoginReq;
import com.zgnba.clos.form.req.UserQueryReq;
import com.zgnba.clos.form.req.UserResetPasswordReq;
import com.zgnba.clos.form.req.UserSaveReq;
import com.zgnba.clos.form.resp.PageResp;
import com.zgnba.clos.form.resp.UserLoginResq;
import com.zgnba.clos.form.resp.UserQueryResp;
import com.zgnba.clos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户模块网络接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${clos.jwt.cache-expire}")
    private Integer cacheExpire;

    @GetMapping("/list")
    @ApiOperation("查询用户")
    public Result list(@Valid UserQueryReq req) {
        PageResp<UserQueryResp> userList = userService.list(req);
        return Result.ok().put("content", userList);
    }

    @PostMapping("/save")
    @ApiOperation("编辑用户")
    public Result save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        String save = userService.save(req);
        if (!ObjectUtils.isEmpty(save)){
            UserLoginReq userLoginReq = CopyUtil.copy(req, UserLoginReq.class);
            req.setPassword(DigestUtils.md5DigestAsHex(userLoginReq.getPassword().getBytes()));
            UserLoginResq userLoginResq = userService.login(userLoginReq);
            String token = jwtUtil.createToken(save);
            userLoginResq.setToken(token);
            saveCacheToken(token, userLoginResq.getId());
            log.info("生成单点登录token: {}, 并放入redis中", token);
            return Result.ok().put("content", userLoginResq);
        }
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户")
    @RequiresPermissions(value = {"ROOT","USER:DELETE"}, logical = Logical.OR)
    public Result delete(@PathVariable String id) {
        userService.delete(id);
        return Result.ok();
    }

    @PostMapping("/reset-password")
    @ApiOperation("重设用户密码")
    public Result resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return Result.ok();
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        UserLoginResq userLoginResq = userService.login(req);
        String token = jwtUtil.createToken(userLoginResq.getId());
        userLoginResq.setToken(token);
        saveCacheToken(token, userLoginResq.getId());
        log.info("生成单点登录token: {}, 并放入redis中", token);
        return Result.ok().put("content", userLoginResq);
    }

    @GetMapping("/logout/{token}")
    @ApiOperation("登出用户")
    public Result logout(@PathVariable String token) {
        redisTemplate.delete(token);
        log.info("从redis中删除token: {}", token);
        return Result.ok();
    }

    private void saveCacheToken(String token, String userId) {
        redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
    }
}
