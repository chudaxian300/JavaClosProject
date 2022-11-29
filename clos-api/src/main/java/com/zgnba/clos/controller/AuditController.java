package com.zgnba.clos.controller;

import com.zgnba.clos.common.utils.JwtUtil;
import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.form.req.AuditQueryReq;
import com.zgnba.clos.form.req.AuditSaveReq;
import com.zgnba.clos.form.resp.AuditQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import com.zgnba.clos.service.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api("审核模块网络接口")
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @ApiOperation("查询待审核帖子")
    @RequiresPermissions(value = {"ROOT","POST:SELECT"}, logical = Logical.OR)
    public Result list(@Valid AuditQueryReq req) {
        PageResp<AuditQueryResp> list = auditService.list(req);
        return Result.ok().put("content", list);
    }

    @PostMapping("/save")
    @ApiOperation("编辑待审核帖子")
    @RequiresPermissions(value = {"ROOT","POST:UPDATE"}, logical = Logical.OR)
    public Result save(@Valid @RequestBody AuditSaveReq req) {
        auditService.save(req);
        return Result.ok();
    }

    @DeleteMapping("/delete/{idsStr}")
    @ApiOperation("删除待审核帖子")
    @RequiresPermissions(value = {"ROOT","POST:DELETE"}, logical = Logical.OR)
    public Result delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        auditService.delete(list);
        return Result.ok();
    }

    @GetMapping("/find-content/{id}")
    @ApiOperation("查询待审核帖子内容")
    @RequiresPermissions(value = {"ROOT","POST:SELECT"}, logical = Logical.OR)
    public Result findContent(@PathVariable String id) {
        String content = auditService.findContent(id);
        return Result.ok().put("content", content);
    }

    @GetMapping("/publish/{id}")
    @ApiOperation("发布帖子内容")
    @RequiresPermissions(value = {"ROOT","POST:PUBLISH"}, logical = Logical.OR)
    public Result publish(@PathVariable String id) {
        auditService.publish(id);
        return Result.ok();
    }


}