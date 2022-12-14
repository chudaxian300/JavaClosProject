package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "文章保存参数校验类")
public class DocSaveReq {
    private String id;

    @NotNull(message = "【创建者】不能为空")
    @ApiModelProperty(value = "创建者",  hidden = true)
    private String creator;

    @NotNull(message = "【名称】不能为空")
    @ApiModelProperty(value = "待审核文章名", required = true)
    private String name;

    @ApiModelProperty(value = "文章内容", required = true)
    private String content;

    @ApiModelProperty(value = "阅读量",  hidden = true)
    private Integer viewCount;

    @ApiModelProperty(value = "点赞量",  hidden = true)
    private Integer voteCount;

}
