package com.zgnba.clos.db.domain;

public class Collect {
    private String id;

    private String collectDoc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectDoc() {
        return collectDoc;
    }

    public void setCollectDoc(String collectDoc) {
        this.collectDoc = collectDoc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", collectDoc=").append(collectDoc);
        sb.append("]");
        return sb.toString();
    }
}