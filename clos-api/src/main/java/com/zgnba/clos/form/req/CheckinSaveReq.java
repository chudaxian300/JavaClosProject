package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "签到保存参数校验类")
public class CheckinSaveReq {
    private String id;

    @ApiModelProperty(value = "签到者ID", example = "(不填)")
    private String userId;

    @NotNull(message = "【学院】不能为空")
    @ApiModelProperty(value = "学院名", required = true)
    private String academy;

    @ApiModelProperty(value = "班级名")
    private String className;

    @ApiModelProperty(value = "图片路径", example = "(不填)")
    private String image;

    @ApiModelProperty(value = "创建时间", example = "(不填)")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CheckinSaveReq{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", academy='" + academy + '\'' +
                ", className='" + className + '\'' +
                ", image=" + image +
                ", createTime=" + createTime +
                '}';
    }
}