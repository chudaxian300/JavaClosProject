package com.zgnba.clos.form.resp;

import lombok.Data;

@Data
public class DocQueryResp {
    private String id;

    private String name;

    private Integer viewCount;

    private Integer voteCount;

    private String creator;
}