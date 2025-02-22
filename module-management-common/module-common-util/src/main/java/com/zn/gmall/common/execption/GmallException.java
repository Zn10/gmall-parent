package com.zn.gmall.common.execption;

import com.zn.gmall.common.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 自定义全局异常类
 * </p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "自定义全局异常类")
public class GmallException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message 错误消息提示
     * @param code    状态码
     */
    public GmallException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum 枚举对象
     */
    public GmallException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}
