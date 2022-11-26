package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AuditSaveReq {

    private String id;

    @NotNull(message = "【有效性】不能为空")
    private Integer valid;

    @NotNull(message = "【创建者】不能为空")
    private String creator;

    @NotNull(message = "【名称】不能为空")
    private String name;

    @NotNull(message = "【内容】不能为空")
    private String content;
}
