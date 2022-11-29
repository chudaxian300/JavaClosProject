package com.zgnba.clos.controller;

import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.JwtUtil;
import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.db.mapper.AuditMapper;
import com.zgnba.clos.form.req.AuditSaveReq;
import com.zgnba.clos.form.req.DocQueryReq;
import com.zgnba.clos.form.req.DocSaveReq;
import com.zgnba.clos.form.resp.DocQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import com.zgnba.clos.service.AuditService;
import com.zgnba.clos.service.DocService;
import com.zgnba.clos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api("论坛模块网络接口")
@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @ApiOperation("查询帖子")
    public Result list(@Valid DocQueryReq req) {
        PageResp<DocQueryResp> list = docService.list(req);
        return Result.ok().put("content", list);
    }

    @PostMapping("/save")
    @ApiOperation("编辑帖子")
    public Result save(@Valid @RequestBody DocSaveReq req, @RequestHeader("token") @ApiIgnore String token) {
        String userId = jwtUtil.getUserId(token);
        String s = userService.selectByUserId(userId);
        req.setCreator(s);
        AuditSaveReq auditSaveReq = CopyUtil.copy(req, AuditSaveReq.class);
        auditService.save(auditSaveReq);
        return Result.ok();
    }

    @DeleteMapping("/delete/{idsStr}")
    @ApiOperation("删除帖子")
    public Result delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return Result.ok();
    }

    @GetMapping("/find-content/{id}")
    @ApiOperation("查询帖子内容")
    public Result findContent(@PathVariable String id) {
        String content = docService.findContent(id);
        return Result.ok().put("content", content);
    }

    @GetMapping("/vote/{id}")
    @ApiOperation("点赞帖子")
    public Result vote(@PathVariable String id) {
        docService.vote(id);
        return Result.ok();
    }

    @GetMapping("/collect/{id}")
    @ApiOperation("收藏帖子")
    public Result vote(@PathVariable String id, @RequestHeader("token") @ApiIgnore String token) {
        String userId = jwtUtil.getUserId(token);
        docService.collect(id, userId);
        return Result.ok();
    }
}