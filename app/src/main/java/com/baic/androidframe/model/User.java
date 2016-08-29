package com.baic.androidframe.model;

import java.io.Serializable;

/**
 * Created by baic on 16/5/6.
 */

public class User implements Serializable{
    private String id;
    private String bigimgurl;
    private String headimgurl;

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBigimgurl() {
        return bigimgurl;
    }

    public void setBigimgurl(String bigimgurl) {
        this.bigimgurl = bigimgurl;
    }
}
