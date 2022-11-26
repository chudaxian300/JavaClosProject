package com.zgnba.clos.exception;

public enum ClosExceptionCode {

    USER_LOGIN_NAME_EXIST("登录名已存在"),
    LOGIN_USER_ERROR("用户名不存在或密码错误"),
    ACCOUNT_ERROR("账号已被锁定, 请联系管理员"),
    VOTE_REPEAT("您已点赞过"),
    FILE_ERROR("文件处理错误"),
    ;

    private String desc;

    ClosExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
