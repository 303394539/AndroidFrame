package com.baic.net.api.model;

import java.io.Serializable;

/**
 * Created by baic on 16/4/19.
 */
public class Response<T> implements Serializable{
    private String contentType;
    private String openid;
    private String passport;
    private T obj;
    private String errmsg;
    private JsParams jsParams;
    private int flag;
    private int issubscribe;
    private int code;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public JsParams getJsParams() {
        return jsParams;
    }

    public void setJsParams(JsParams jsParams) {
        this.jsParams = jsParams;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIssubscribe() {
        return issubscribe;
    }

    public void setIssubscribe(int issubscribe) {
        this.issubscribe = issubscribe;
    }

    public class JsParams implements Serializable{
        private long timestamp;
        private String noncestr;
        private String signature;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
