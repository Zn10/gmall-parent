package com.zn.gmall.common.handler;

import com.zn.gmall.common.execption.GmallException;
import com.zn.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> error(Exception e) {
        log.error("exception message", e);
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result<String> error(GmallException e) {
        log.error("exception message", e);
        return Result.fail(e.getMessage());
    }
}
