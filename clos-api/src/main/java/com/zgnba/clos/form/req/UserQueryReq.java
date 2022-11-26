package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class UserQueryReq extends PageReq{

    private String loginName;
}
