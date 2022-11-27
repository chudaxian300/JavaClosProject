package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户修改密码参数校验类")
public class UserResetPasswordReq {
    private String id;


    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "【密码】不能为空")
    private String password;
}
