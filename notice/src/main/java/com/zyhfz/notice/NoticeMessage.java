package com.zyhfz.notice;

import java.io.Serializable;

/**
 * Created by baic on 16/5/17.
 */
public class NoticeMessage implements Serializable {

    private Enum key;
    private String value;
    private Object valueObj;

    public NoticeMessage(Enum key, String value) {
        this.key = key;
        this.value = value;
    }

    public NoticeMessage(Enum key, Object valueObj) {
        this.key = key;
        this.valueObj = valueObj;
    }

    public Enum getKey() {
        return key;
    }

    public void setKey(Enum key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getValueObj() {
        return valueObj;
    }

    public void setValueObj(Object valueObj) {
        this.valueObj = valueObj;
    }
}
