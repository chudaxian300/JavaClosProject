package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "文章内容查询校验类")
public class DocQueryReq extends PageReq {

    private String ids;
}
