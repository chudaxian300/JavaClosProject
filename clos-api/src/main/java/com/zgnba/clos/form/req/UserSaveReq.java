package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UserSaveReq {
    private String id;

    @NotNull(message = "【用户名】不能为空")
    private String loginName;

    @NotNull(message = "【昵称】不能为空")
    private String name;

    @NotNull(message = "【密码】不能为空")
    private String password;

    @NotNull(message = "【角色】不能为空")
    private String role;
}