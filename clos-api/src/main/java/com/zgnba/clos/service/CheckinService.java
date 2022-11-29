package com.zgnba.clos.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.FileUtil;
import com.zgnba.clos.common.utils.RedisUtil;
import com.zgnba.clos.common.utils.SnowFlake;
import com.zgnba.clos.db.domain.Checkin;
import com.zgnba.clos.db.domain.CheckinExample;
import com.zgnba.clos.db.domain.User;
import com.zgnba.clos.db.mapper.AcademyMapper;
import com.zgnba.clos.db.mapper.CheckinMapper;
import com.zgnba.clos.db.mapper.UserMapper;
import com.zgnba.clos.form.req.CheckinQueryReq;
import com.zgnba.clos.form.req.CheckinSaveReq;
import com.zgnba.clos.form.resp.CheckinQueryResp;
import com.zgnba.clos.form.resp.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class CheckinService {

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private AcademyMapper academyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    public RedisUtil redisUtil;

    @Value("${clos.filePath.checkinFile}")
    private String checkinFilePath;

    public PageResp<CheckinQueryResp> list(CheckinQueryReq req) {
        CheckinExample checkinExample = new CheckinExample();
        CheckinExample.Criteria criteria = checkinExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getAcademy())) {
            criteria.andAcademyEqualTo(req.getAcademy());
        }
        if (!ObjectUtils.isEmpty(req.getClassName())) {
            criteria.andClassNameEqualTo(req.getClassName());
        }
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andUserEqualTo(req.getName());
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Checkin> checkinList = checkinMapper.selectByExample(checkinExample);

        PageInfo<Checkin> pageInfo = new PageInfo<>(checkinList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<CheckinQueryResp> list = CopyUtil.copyList(checkinList, CheckinQueryResp.class);

        PageResp<CheckinQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    @Transactional
    public void save(CheckinSaveReq req, MultipartFile multipartFile) {
        Checkin checkin = CopyUtil.copy(req, Checkin.class);

        StringBuffer fileName = new StringBuffer();
        fileName.append("-");
        fileName.append(req.getUser());
        fileName.append("-");
        fileName.append(req.getAcademy());
        if (!ObjectUtils.isEmpty(checkin.getClassName())){
            fileName.append("-");
            fileName.append(req.getClassName());
        }
        checkinFilePath = checkinFilePath + req.getAcademy() + "\\" + req.getClassName() + "\\";
        FileUtil.delete(checkinFilePath, fileName.toString());
        String filePath = FileUtil.upload(multipartFile, checkinFilePath, fileName.toString());

        if (ObjectUtils.isEmpty(checkin.getId())) {
            DateTime now = DateUtil.date();
            // 新增
            checkin.setAcademy(req.getAcademy());
            checkin.setImage(filePath);
            checkin.setId(String.valueOf(snowFlake.nextId()));
            checkin.setCreateTime(now);
            checkinMapper.insert(checkin);
        } else {
            // 更新
            DateTime now = DateUtil.date();
            checkin.setImage(filePath);
            checkin.setCreateTime(now);
            checkinMapper.updateByPrimaryKey(checkin);
        }
    }

    public void delete(String id) {
        checkinMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        CheckinExample checkinExample = new CheckinExample();
        CheckinExample.Criteria criteria = checkinExample.createCriteria();
        criteria.andIdIn(ids);
    }
}
