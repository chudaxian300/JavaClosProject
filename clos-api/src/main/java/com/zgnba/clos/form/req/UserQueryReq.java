package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户查询参数校验类")
public class UserQueryReq extends PageReq{

    @ApiModelProperty(value = "用户名")
    private String loginName;
}
