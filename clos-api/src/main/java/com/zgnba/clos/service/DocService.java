package com.zgnba.clos.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.RedisUtil;
import com.zgnba.clos.db.domain.*;
import com.zgnba.clos.db.mapper.CollectMapper;
import com.zgnba.clos.db.mapper.ContentMapper;
import com.zgnba.clos.db.mapper.DocMapper;
import com.zgnba.clos.exception.ClosException;
import com.zgnba.clos.exception.ClosExceptionCode;
import com.zgnba.clos.form.req.DocQueryReq;
import com.zgnba.clos.form.req.DocSaveReq;
import com.zgnba.clos.form.resp.DocQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DocService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    public RedisUtil redisUtil;

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getIds())) {
            List<String> list = Arrays.asList(req.getIds());
            criteria.andIdIn(list);
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(docMapper.selectByPrimaryKey(req.getId()))) {
            // 新增
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(String id) {
        docMapper.deleteByPrimaryKey(id);
        contentMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria criteria1 = contentExample.createCriteria();
        criteria1.andIdIn(ids);
        docMapper.deleteByExample(docExample);
        contentMapper.deleteByExample(contentExample);
    }

    public String findContent(String id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 文档阅读数+1
        docMapper.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /**
     * 点赞
     */
    public void vote(String id) {
        if (redisUtil.validateRepeat("DOC_VOTE_" + id, 5000)) {
            docMapper.increaseVoteCount(id);
        } else {
            throw new ClosException(ClosExceptionCode.VOTE_REPEAT);
        }
        // 推送消息
        // rocketMQTemplate.convertAndSend("VOTE_TOPIC", "【" + docDb.getName() + "】被点赞！");
    }

    /**
     * 收藏
     */
    public void collect(String docId, String userId) {
        Collect select = collectMapper.selectByPrimaryKey(userId);
        Collect collect = new Collect();
        if (ObjectUtils.isEmpty(select)) {
            List<String> list = Arrays.asList(docId);
            collect.setId(userId);
            collect.setCollectDoc(String.valueOf(list));
            collectMapper.insert(collect);
        } else {
            String s = select.getCollectDoc();
            String substring = s.substring(1, s.length() - 1);
            String[] split = substring.split(", ");
            List<String> list = new ArrayList<>(Arrays.asList(split));
            if (list.contains(docId)) {
                list.remove(docId);
            } else {
                list.add(docId);
            }
            select.setCollectDoc(String.valueOf(list));
            collectMapper.updateByPrimaryKeyWithBLOBs(select);
        }
    }
}
