package com.baic.net.api.model;

import java.io.Serializable;

/**
 * Created by baic on 16/4/22.
 */
public class PagingRequestParam implements Serializable {
    private Object obj;
    private int offset;
    private int size;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
