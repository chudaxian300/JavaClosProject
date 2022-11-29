package com.zgnba.clos.form.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "签到保存参数校验类")
public class CheckinSaveReq {
    private String id;

    @ApiModelProperty(value = "签到者",  hidden = true)
    private String user;

    @NotNull(message = "【学院】不能为空")
    @ApiModelProperty(value = "学院名", required = true)
    private String academy;

    @ApiModelProperty(value = "班级名")
    private String className;

    @ApiModelProperty(value = "图片路径",  hidden = true)
    private String image;

    @ApiModelProperty(value = "创建时间",  hidden = true)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String userId) {
        this.user = userId;
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
                ", userId='" + user + '\'' +
                ", academy='" + academy + '\'' +
                ", className='" + className + '\'' +
                ", image=" + image +
                ", createTime=" + createTime +
                '}';
    }
}