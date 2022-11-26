package com.zgnba.clos.config.shiro;

import com.zgnba.clos.common.utils.JwtUtil;
import com.zgnba.clos.db.domain.User;
import com.zgnba.clos.db.mapper.UserMapper;
import com.zgnba.clos.exception.ClosException;
import com.zgnba.clos.exception.ClosExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定义了Shiro的认证和授权方法
 */
@Slf4j
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权方法(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        String userId = user.getId();
        // 查询用户权限列表
        Set<String> userPermissions = userMapper.searchUserPermissions(userId);
        log.info("获得用户权限: {}", userPermissions);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 把权限列表添加到info对象中
        info.setStringPermissions(userPermissions);
        return info;
    }

    /**
     * 认证方法(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从令牌中获取userId, 然后检测该账户是否被冻结
        String accessToken = (String) authenticationToken.getPrincipal();
        String userId = jwtUtil.getUserId(accessToken);
        User user = userMapper.selectByPrimaryKey(userId);
        // 离职但token没过期情况
        if (user==null){
            log.warn("账号已被锁定, 请联系管理员");
            throw new ClosException(ClosExceptionCode.ACCOUNT_ERROR);
        }
        // 往info对象中添加用户信息, Token字符串
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
