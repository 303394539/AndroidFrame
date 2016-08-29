package com.baic.net.api.intf;

/**
 * Created by baic on 16/4/22.
 */
public interface SimpleCallback extends Callback {
    void success(Object o);

    void error(int code, String msg);

    void fail(String msg);
}
