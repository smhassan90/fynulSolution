package com.fynuls.dal;

import java.util.List;

public class DropDownResponse {
    private List<KeyValue> keyValueList;
    private String status;

    public List<KeyValue> getKeyValueList() {
        return keyValueList;
    }

    public void setKeyValueList(List<KeyValue> keyValueList) {
        this.keyValueList = keyValueList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
