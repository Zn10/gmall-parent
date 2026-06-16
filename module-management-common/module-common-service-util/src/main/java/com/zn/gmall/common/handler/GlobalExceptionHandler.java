package com.zn.gmall.common.handler;

import com.zn.gmall.common.execption.GmallException;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result<String> handleNullPointerException(NullPointerException e) {
        log.error("全局空指针异常", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        //1、从这个异常中拿到校验结果
        BindingResult bindingResult = exception.getBindingResult();
        //2、把结果整理下返回前端：  {tel:"",consignee:""}
        Map<String,String> errMap = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String field = error.getField(); //错误发生的属性
            String message =
                error.getDefaultMessage(); //错误消息
            errMap.put(field,message);
        }
        return Result.build(errMap, ResultCodeEnum.INVAILD_PARAM);
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
