package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "用户登录参数效验类")
public class UserLoginReq {
    @NotEmpty(message = "【用户名】不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String loginName;

    @NotEmpty(message = "【密码】不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
