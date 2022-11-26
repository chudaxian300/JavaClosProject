package com.zgnba.clos.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.RedisUtil;
import com.zgnba.clos.common.utils.SnowFlake;
import com.zgnba.clos.db.domain.*;
import com.zgnba.clos.db.mapper.CollectMapper;
import com.zgnba.clos.db.mapper.ContentMapper;
import com.zgnba.clos.db.mapper.AuditMapper;
import com.zgnba.clos.db.mapper.DocMapper;
import com.zgnba.clos.exception.ClosException;
import com.zgnba.clos.exception.ClosExceptionCode;
import com.zgnba.clos.form.req.AuditQueryReq;
import com.zgnba.clos.form.req.AuditSaveReq;
import com.zgnba.clos.form.req.DocSaveReq;
import com.zgnba.clos.form.resp.AuditQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class AuditService {

    @Autowired
    private AuditMapper auditMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private DocService docService;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    public RedisUtil redisUtil;

    public PageResp<AuditQueryResp> list(AuditQueryReq req) {
        AuditExample auditExample = new AuditExample();
        AuditExample.Criteria criteria = auditExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getId())){
            List<String> list = Arrays.asList(req.getId());
            criteria.andIdIn(list);
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Audit> auditList = auditMapper.selectByExample(auditExample);

        PageInfo<Audit> pageInfo = new PageInfo<>(auditList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<AuditQueryResp> list = CopyUtil.copyList(auditList, AuditQueryResp.class);

        PageResp<AuditQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    @Transactional
    public void save(AuditSaveReq req) {
        Audit audit = CopyUtil.copy(req, Audit.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            audit.setId(String.valueOf(snowFlake.nextId()));
            audit.setValid(0);
            auditMapper.insert(audit);

            content.setId(audit.getId());
            contentMapper.insert(content);
        } else {
            // 更新
            auditMapper.updateByPrimaryKey(audit);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void publish(String id){
        Audit audit = auditMapper.selectByPrimaryKey(id);
        if (audit.getValid() == 1){
            DocSaveReq doc = CopyUtil.copy(audit, DocSaveReq.class);
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docService.save(doc);
        }
    }

    public void delete(String id) {
        auditMapper.deleteByPrimaryKey(id);
        contentMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        AuditExample auditExample = new AuditExample();
        AuditExample.Criteria criteria = auditExample.createCriteria();
        criteria.andIdIn(ids);
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria criteria1 = contentExample.createCriteria();
        criteria1.andIdIn(ids);
        auditMapper.deleteByExample(auditExample);
        contentMapper.deleteByExample(contentExample);
    }

    public String findContent(String id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }
}
