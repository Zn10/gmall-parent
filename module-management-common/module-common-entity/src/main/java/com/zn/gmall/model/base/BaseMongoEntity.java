package com.zn.gmall.model.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseMongoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Id
    private String id;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @LastModifiedDate
    private Date updateTime;

}
