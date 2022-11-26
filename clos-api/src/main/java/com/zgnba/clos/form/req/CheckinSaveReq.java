package com.zgnba.clos.form.req;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class CheckinSaveReq {
    private String id;

    private String userId;

    @NotNull(message = "【学院】不能为空")
    private String academy;

    private String className;

    private String image;

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