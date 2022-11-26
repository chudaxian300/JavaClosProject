package com.zgnba.clos.config;


import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.exception.ClosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理、数据预处理等
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * 校验异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result validExceptionHandler(BindException e) {
        log.warn("参数校验失败: {}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 校验异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ClosException.class)
    @ResponseBody
    public Result validExceptionHandler(ClosException e) {
        log.warn("业务异常: {}", e.getCode().getDesc());
        return Result.error(e.getCode().getDesc());
    }

    /**
     * 校验异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result validExceptionHandler(Exception e) {
        log.error("系统异常: ", e);
        return Result.error("系统出现异常，请联系管理员");
    }
}
