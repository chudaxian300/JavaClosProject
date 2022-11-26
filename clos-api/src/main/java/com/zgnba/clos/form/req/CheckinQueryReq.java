package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class CheckinQueryReq extends PageReq {

    private String academy;

    private String name;

    private String className;


}
