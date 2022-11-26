package com.zgnba.clos.db.domain;

import java.util.Date;

public class Checkin {
    private String id;

    private String userId;

    private Integer academyId;

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

    public Integer getAcademyId() {
        return academyId;
    }

    public void setAcademyId(Integer academyId) {
        this.academyId = academyId;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", academyId=").append(academyId);
        sb.append(", className=").append(className);
        sb.append(", image=").append(image);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}