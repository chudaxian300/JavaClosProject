package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "审核数据编辑参数校验类")
public class AuditSaveReq {

    @ApiModelProperty(value = "数据ID", allowEmptyValue = true)
    private String id;

    @NotNull(message = "【有效性】不能为空")
    @ApiModelProperty(value = "数据ID", required = true)
    private Integer valid;

    @NotNull(message = "【创建者】不能为空")
    @ApiModelProperty(value = "创建者ID", example = "(不填)")
    private String creator;

    @NotNull(message = "【名称】不能为空")
    @ApiModelProperty(value = "待审核文章名", required = true)
    private String name;

    @NotNull(message = "【内容】不能为空")
    @ApiModelProperty(value = "待审核文章内容", required = true)
    private String content;
}
