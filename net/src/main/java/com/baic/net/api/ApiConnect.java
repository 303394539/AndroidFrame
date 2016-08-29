package com.baic.net.api;

import android.util.Log;

import com.baic.net.api.intf.Callback;
import com.baic.net.api.intf.CompleteCallback;
import com.baic.net.api.intf.PagingCallback;
import com.baic.net.api.intf.SimpleCallback;
import com.baic.net.api.model.PagingRequestParam;
import com.baic.net.api.model.PagingResponseObj;
import com.baic.net.api.model.Request;
import com.baic.net.api.model.Response;
import com.baic.utils.JsonUtil;
import com.baic.utils.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;

/**
 * Created by baic on 16/4/22.
 */
public class ApiConnect {
    private Request request;
    private Callback callback;
    private int offset;
    private int size;
    private Call<Response> call;
    private Class<?> itemClass;
    private String jdomain;
    private String japi;
    private String sign;

    public static int COUNT = 0;

    public static final int SIZE_DEFAULT = 20;

    public static final int CODE_SUCCESS = 0;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Callback getCallback() {
        return callback;
    }

    public ApiConnect setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public ApiConnect setParams(Serializable params) {
        this.request.setRequestParam(params);
        return this;
    }

    public ApiConnect setOpenid(String openid) {
        this.request.setOpenid(openid);
        return this;
    }

    public ApiConnect setPassport(String passport) {
        this.request.setPassport(passport);
        return this;
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

    public Class<?> getItemClass() {
        return itemClass;
    }

    public ApiConnect setItemClass(Class<?> itemClass) {
        this.itemClass = itemClass;
        return this;
    }

    public void get() {
        if (!StringUtil.isNullOrEmpty(jdomain) && !StringUtil.isNullOrEmpty(japi)) {
            get(JsonUtil.objectToString(request), jdomain, japi);
        } else {
            get(JsonUtil.objectToString(request));
        }
    }

    public void post() {
        if (!StringUtil.isNullOrEmpty(jdomain) && !StringUtil.isNullOrEmpty(japi)) {
            post(JsonUtil.objectToString(request), jdomain, japi);
        } else {
            post(JsonUtil.objectToString(request));
        }
    }

    public void get(String j) {
        this.sign = StringUtil.getMd5(j);
        enqueue(Api.apiService.get(Api.apiConfig.getPathname(), j, String.valueOf(++COUNT), sign));
    }

    public void post(String j) {
        this.sign = StringUtil.getMd5(j);
        enqueue(Api.apiService.post(Api.apiConfig.getPathname(), j, String.valueOf(++COUNT), sign));
    }

    public void get(String j, String jdomain, String japi) {
        this.sign = StringUtil.getMd5(j);
        enqueue(Api.apiService.get(Api.apiConfig.getPathname(), j, String.valueOf(++COUNT), sign, jdomain, japi));
    }

    public void post(String j, String jdomain, String japi) {
        this.sign = StringUtil.getMd5(j);
        enqueue(Api.apiService.post(Api.apiConfig.getPathname(), j, String.valueOf(++COUNT), sign, jdomain, japi));
    }

    public void next(Integer offset, Integer size, String jdomain, String japi) {
        if (offset != null) {
            this.offset = offset;
        }
        if (size != null) {
            this.size = size;
        } else {
            if (this.size == 0) {
                this.size = SIZE_DEFAULT;
            }
        }
        if (!StringUtil.isNullOrEmpty(jdomain)) {
            this.jdomain = jdomain;
        }
        if (!StringUtil.isNullOrEmpty(japi)) {
            this.japi = japi;
        }
        Object requestParam = request.getRequestParam();
        if (requestParam instanceof PagingRequestParam) {
            PagingRequestParam pagingRequestParam = (PagingRequestParam) requestParam;
            pagingRequestParam.setSize(this.size);
            pagingRequestParam.setOffset(this.offset);
            request.setRequestParam(pagingRequestParam);
        } else {
            Map<String, Object> map = JsonUtil.jsonToObject(JsonUtil.objectToJson(requestParam), Map.class, String.class, Object.class);
            map.put("offset", this.offset);
            map.put("size", this.size);
            request.setRequestParam(map);
        }
        this.offset += this.size;
        this.get();
    }

    public void next() {
        next(null, null, null, null);
    }

    public void next(int offset) {
        next(offset, null, null, null);
    }

    public void next(int offset, int size) {
        next(offset, size, null, null);
    }

    public void cancel() {
        call.cancel();
    }

    public boolean isExecuted() {
        return call.isExecuted();
    }

    public boolean isCanceled() {
        return call.isCanceled();
    }

    private void enqueue(Call<Response> call) {
        this.call = call;
        final int hasCode = this.call.hashCode();
        log4ApiUrl(hasCode, Api.apiConfig.getBaseUrl(), Api.apiConfig.getPathname());
        log4j(hasCode, JsonUtil.objectToString(request));
        log4Ljsonp(hasCode, jdomain, japi);
        log4Sign(hasCode, sign);
        try {
            call.enqueue(new retrofit2.Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Response r = response.body();
                    if (r == null) {
                        try {
                            throw new IllegalArgumentException(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    log4Response(hasCode, r);
                    if (getCallback() instanceof SimpleCallback) {
                        SimpleCallback c = (SimpleCallback) callback;
                        if (r.getCode() == CODE_SUCCESS) {
                            Object o = r.getObj();
                            if (itemClass != null && r.getObj() != null) {
                                o = JsonUtil.objectToObject(r.getObj(), itemClass);
                            }
                            c.success(o);
                        } else {
                            c.error(r.getCode(), r.getErrmsg());
                        }
                    } else if (getCallback() instanceof PagingCallback) {
                        PagingCallback c = (PagingCallback) callback;
                        if (r.getCode() == CODE_SUCCESS) {
                            PagingResponseObj pagingResponseObj = JsonUtil.objectToObject(r.getObj(), PagingResponseObj.class, Object.class);
                            List list = pagingResponseObj.getList();
                            if (itemClass != null && list != null) {
                                pagingResponseObj.setList(JsonUtil.<ArrayList<Object>>objectToObject(list, List.class, itemClass));
                            }
                            c.success(pagingResponseObj);
                        } else {
                            c.error(r.getCode(), r.getErrmsg());
                        }
                    } else if (getCallback() instanceof CompleteCallback) {
                        CompleteCallback c = (CompleteCallback) callback;
                        if (r.getCode() == CODE_SUCCESS) {
                            Object o = r.getObj();
                            if (itemClass != null && r.getObj() != null) {
                                o = JsonUtil.objectToObject(r.getObj(), itemClass);
                            }
                            c.success(r, o);
                        } else {
                            c.error(r, r.getCode(), r.getErrmsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    log4Error(hasCode, t.getMessage());
                    if (getCallback() instanceof SimpleCallback) {
                        SimpleCallback c = (SimpleCallback) callback;
                        c.fail(t.getMessage());
                    } else if (getCallback() instanceof PagingCallback) {
                        PagingCallback c = (PagingCallback) callback;
                        c.fail(t.getMessage());
                    } else if (getCallback() instanceof CompleteCallback) {
                        CompleteCallback c = (CompleteCallback) callback;
                        c.fail(t, request, t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log4j(int index, String j) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        Log.d(String.format("==========> Api(%d) %s", index, "j"), j);
    }

    private void log4ApiUrl(int index, String baseUrl, String pathname) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        Log.d(String.format("==========> Api(%d) %s", index, "apiUrl"), baseUrl + pathname);
    }

    private void log4Response(int index, Response response) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        Log.d(String.format("==========> Api(%d) %s", index, "response"), JsonUtil.objectToString(response));
        Log.d(String.format("==========> Api(%d) %s", index, "result"), JsonUtil.objectToString(response.getObj()));
    }

    private void log4Ljsonp(int index, String jdomain, String japi) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        if (!StringUtil.isNullOrEmpty(jdomain)) {
            Log.d(String.format("==========> Api(%d) %s", index, "jdomain"), jdomain);
        }
        if (!StringUtil.isNullOrEmpty(japi)) {
            Log.d(String.format("==========> Api(%d) %s", index, "japi"), japi);
        }
    }

    private void log4Error(int index, String msg) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        Log.i(String.format("==========> Api(%d) %s", index, "error"), JsonUtil.objectToString(msg));
    }

    private void log4Sign(int index, String sign) {
        if (!Api.apiConfig.isLoggable()) {
            return;
        }
        Log.i(String.format("==========> Api(%d) %s", index, "sign"), sign);
    }

    public String getJdomain() {
        return jdomain;
    }

    public void setJdomain(String jdomain) {
        this.jdomain = jdomain;
    }

    public String getJapi() {
        return japi;
    }

    public void setJapi(String japi) {
        this.japi = japi;
    }
}
