package com.zgnba.clos.form.resp;

import lombok.Data;

@Data
public class UserQueryResp {
    private String id;

    private String loginName;

    private String name;

    private String password;

    private String role;
}