package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UserResetPasswordReq {
    private String id;

    @NotNull(message = "【密码】不能为空")
    // @Length(min = 6, max = 20, message = "【密码】6~20位")
    // @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]$", message = "【密码】至少包含 数字和英文")
    private String password;
}
