package com.zgnba.clos.controller;

import com.zgnba.clos.common.utils.JwtUtil;
import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.form.req.CheckinQueryReq;
import com.zgnba.clos.form.req.CheckinSaveReq;
import com.zgnba.clos.form.resp.CheckinQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import com.zgnba.clos.service.CheckinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api("核酸打卡模块网络接口")
@RestController
@RequestMapping("/checkin")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @ApiOperation("查询签到")
    public Result list(@Valid CheckinQueryReq req) {
        PageResp<CheckinQueryResp> list = checkinService.list(req);
        return Result.ok().put("content", list);
    }

    @PostMapping("/save")
    @ApiOperation("编辑签到")
    public Result save(@Valid CheckinSaveReq req, @RequestPart("file") MultipartFile file, @RequestHeader("token") @ApiIgnore String token) {
        String userId = jwtUtil.getUserId(token);
        req.setUserId(userId);
        checkinService.save(req, file);
        return Result.ok();
    }
    // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Njk4MTE5NTAsInVzZXJJZCI6IjEifQ.5RM7aJB0hktJm6AlqvplQhE6fP1h8DHK0-QCkisxBvc

    @DeleteMapping("/delete/{idsStr}")
    @ApiOperation("删除签到")
    @RequiresPermissions(value = {"ROOT","CHECKIN:DELETE"}, logical = Logical.OR)
    public Result delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        checkinService.delete(list);
        return Result.ok();
    }
}