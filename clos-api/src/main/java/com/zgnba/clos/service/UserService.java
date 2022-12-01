package com.zgnba.clos.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zgnba.clos.common.utils.CopyUtil;
import com.zgnba.clos.common.utils.SnowFlake;
import com.zgnba.clos.db.domain.User;
import com.zgnba.clos.db.domain.UserExample;
import com.zgnba.clos.db.mapper.UserMapper;
import com.zgnba.clos.exception.ClosException;
import com.zgnba.clos.exception.ClosExceptionCode;
import com.zgnba.clos.form.req.UserLoginReq;
import com.zgnba.clos.form.req.UserQueryReq;
import com.zgnba.clos.form.req.UserResetPasswordReq;
import com.zgnba.clos.form.req.UserSaveReq;
import com.zgnba.clos.form.resp.PageResp;
import com.zgnba.clos.form.resp.UserLoginResq;
import com.zgnba.clos.form.resp.UserQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<User> userList = userMapper.selectByExampleWithBLOBs(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public String save(UserSaveReq user) {
        User user1 = CopyUtil.copy(user, User.class);
        if (ObjectUtils.isEmpty(user.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(user.getLoginName()))) {
                String id = String.valueOf(snowFlake.nextId());
                user1.setId(id);
                user1.setRole("[2]");
                userMapper.insert(user1);
                return id;
            } else {
                throw new ClosException(ClosExceptionCode.LOGIN_USER_ERROR);
            }
        } else {
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user1);
            return null;
        }
    }

    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResq login(UserLoginReq req) {
        User user = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(user)) {
            log.info("用户名不存在, {}", req.getLoginName());
            throw new ClosException(ClosExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(req.getPassword())) {
                UserLoginResq userLoginResq = CopyUtil.copy(user, UserLoginResq.class);
                Set<String> permissions = userMapper.searchUserPermissions(user.getId());
                Set<String> role = userMapper.searchUserRole(user.getId());
                log.info("查询用户权限: {}", permissions);
                log.info("查询用户角色: {}", role);
                userLoginResq.setPermission(permissions);
                userLoginResq.setRole(role);
                return userLoginResq;
            } else {
                log.info("密码不对, 输入密码: {}, 数据库密码: {}", req.getPassword(), user.getPassword());
                throw new ClosException(ClosExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public String selectByUserId(String userId) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdEqualTo(userId);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0).getName();
        }
    }

}
