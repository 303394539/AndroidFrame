package com.baic.net.api.intf;

import com.baic.net.api.model.Request;
import com.baic.net.api.model.Response;

/**
 * Created by baic on 16/4/22.
 */
public interface CompleteCallback<T> extends Callback {
    void success(Response<T> response, T o);

    void error(Response<T> response, int code, String msg);

    void fail(Throwable t, Request request, String msg);
}
