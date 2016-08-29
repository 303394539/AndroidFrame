package com.baic.net.api.intf;

import com.baic.net.api.model.PagingResponseObj;

/**
 * Created by baic on 16/5/6.
 */
public interface PagingCallback extends Callback {
    void success(PagingResponseObj o);

    void error(int code, String msg);

    void fail(String msg);
}
