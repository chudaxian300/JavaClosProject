package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "签到信息查询参数校验类")
public class CheckinQueryReq extends PageReq {

    @ApiModelProperty(value = "查询学院")
    private String academy;

    @ApiModelProperty(value = "查询个人名称")
    private String name;

    @ApiModelProperty(value = "查询班级")
    private String className;


}
