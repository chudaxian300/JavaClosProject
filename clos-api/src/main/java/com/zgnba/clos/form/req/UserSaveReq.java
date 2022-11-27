package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户编辑参数校验类")
public class UserSaveReq {
    private String id;

    @NotEmpty(message = "【用户名】不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String loginName;

    @NotEmpty(message = "【密码】不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotNull(message = "【昵称】不能为空")
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @NotNull(message = "【角色】不能为空")
    @ApiModelProperty(value = "用户角色", example = "[1]", required = true)
    private String role;
}