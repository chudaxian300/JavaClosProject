package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "分页参数校验类")
public class PageReq {

    @NotNull(message = "【页码】不能为空")
    @ApiModelProperty(value = "页码", required = true)
    private int page;

    @NotNull(message = "【每条页数】不能为空")
    @Max(value = 1000, message = "【每条页数】不能超过1000")
    @ApiModelProperty(value = "每页数据量", required = true)
    private int pageSize;
}
