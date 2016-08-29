package com.baic.cache.database;

import java.io.Serializable;

/**
 * Created by baic on 16/5/13.
 */
public class Data implements Serializable{
    private String key;
    private String contentId;
    private String content;
    private long time;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
