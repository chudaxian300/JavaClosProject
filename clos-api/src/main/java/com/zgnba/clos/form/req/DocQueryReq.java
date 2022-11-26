package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class DocQueryReq extends PageReq {

    private String ids;
}
