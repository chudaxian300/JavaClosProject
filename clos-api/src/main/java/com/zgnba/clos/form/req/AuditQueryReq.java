package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "审核数据查询参数校验类")
public class AuditQueryReq extends PageReq{

    @ApiModelProperty(value = "数据ID", allowEmptyValue = true)
    private String id;

    @ApiModelProperty(value = "是否过审", allowEmptyValue = true)
    private Integer valid;
}
