package com.zgnba.clos.form.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserLoginResq implements Serializable {
    private String id;

    private String loginName;

    private String name;

    private Set<String> role;

    private Set<String> permission;

    private String token;
}
