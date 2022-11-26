package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AuditQueryReq extends PageReq{

    private String id;

    private Integer valid;
}
