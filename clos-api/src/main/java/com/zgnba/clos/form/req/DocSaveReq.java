package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DocSaveReq {
    private String id;

    @NotNull(message = "【名称】不能为空")
    private String name;

    @NotNull(message = "【创建者】不能为空")
    private String creator;

    private Integer viewCount;

    private Integer voteCount;

    @NotNull(message = "【内容】不能为空")
    private String content;

}
