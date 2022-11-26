package com.zgnba.clos.config.shiro;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zgnba.clos.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 定义了Shiro的Filter功能
 * 创建过滤器类, 判断用户提交令牌有没有问题
 */
@Component
@Slf4j
@Scope("prototype")
public class OAuth2Filter extends AuthenticatingFilter {

    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${clos.jwt.cache-expire}")
    private Integer cacheExpire;

    /**
     * 拦截请求，把token封装成token令牌对象，供shiro使用
     *
     * @return OAuth2Token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = getRequestToken(request);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        return new OAuth2Token(token);
    }

    /**
     * 拦截请求, 判断该请求是否应该由shiro处理
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        return req.getMethod().equals(RequestMethod.OPTIONS.name());
    }

    /**
     * shrio处理请求逻辑方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("------------- LoginInterceptor 开始 -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        String path = request.getRequestURL().toString();
        log.info("接口登录拦截: path：{}", path);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        threadLocalToken.clear();

        // 判断请求头有没有令牌
        String token = getRequestToken(request);
        if (StrUtil.isBlank(token)) {
            log.info( "token为空，请求被拦截" );
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().print("无效的令牌");
            logEnd(startTime);
            return false;
        }
        // 验证令牌有效性, 是否过期
        try {
            jwtUtil.verifierToken(token);
            String userId = jwtUtil.getUserId(token);
            log.info("令牌验证成功, 已登录: {}", userId);
        } catch (TokenExpiredException e) {
            if (redisTemplate.hasKey(token)) {
                redisTemplate.delete(token);
                String userId = jwtUtil.getUserId(token);
                token = jwtUtil.createToken(userId);
                redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
                threadLocalToken.setToken(token);
                log.info("超时令牌刷新成功: {}", userId);
            } else {
                log.warn( "token已过期，请求被拦截" );
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                response.getWriter().print("令牌已过期");
                logEnd(startTime);
                return false;
            }
        } catch (Exception e) {
            log.warn( "token无效，请求被拦截" );
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().print("无效的令牌");
            logEnd(startTime);
            return false;
        }
        // token没问题, 进入realm阶段, 间接调用realm类
        boolean flag = executeLogin(request, response);
        logEnd(startTime);
        return flag;
    }

    /**
     * executeLogin(request, response)认证方法失败后执行此方法
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        log.warn("Realm认证失败, 请求被拦截");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            resp.getWriter().print(e.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        long startTime = (Long) request.getAttribute("requestStartTime");
        logEnd(startTime);
        return false;
    }

    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        super.doFilterInternal(request, response, chain);
    }

    private void logEnd(long startTime){
        log.info("------------- LoginInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }

    /**
     * 从请求获取token字符串方法
     */
    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        log.info("登录校验开始, token: {}", token);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }
}
