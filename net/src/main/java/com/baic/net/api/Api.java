package com.baic.net.api;

import android.text.TextUtils;
import android.util.Log;

import com.baic.net.api.annotation.PortAnnotation;
import com.baic.net.api.intf.ApiService;
import com.baic.net.api.intf.Callback;
import com.baic.net.api.model.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.MessageDigest;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by baic on 16/4/21.
 */
public class Api {

    public static ApiConfig apiConfig;

    public static ApiService apiService;

    public static <T> T init(final ApiConfig config, Class<T> portService) {
        apiConfig = config;
        apiService = new Retrofit.Builder().baseUrl(config.getBaseUrl()).addConverterFactory(JacksonConverterFactory.create()).build().create(ApiService.class);
        return (T) Proxy.newProxyInstance(portService.getClassLoader(), new Class<?>[]{portService}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                PortAnnotation requestParams = method.getAnnotation(PortAnnotation.class);
                String action = requestParams.action();
                String openid = requestParams.openid();
                String passport = requestParams.passport();
                String appid = requestParams.wxappid();
                String vericode = requestParams.vericode();
                Integer offset = requestParams.offset();
                Integer size = requestParams.size();
                final Request request = new Request();
                if(!TextUtils.isEmpty(action)){
                    request.setAction(action);
                }
                if (!TextUtils.isEmpty(openid)) {
                    request.setOpenid(openid);
                }else if(!TextUtils.isEmpty(Request.defaultOpenid)){
                    request.setOpenid(Request.defaultOpenid);
                }
                if (!TextUtils.isEmpty(passport)) {
                    request.setPassport(passport);
                }else if(!TextUtils.isEmpty(Request.defaultPassport)){
                    request.setPassport(Request.defaultPassport);
                }
                if (!TextUtils.isEmpty(appid)) {
                    request.setWxappid(appid);
                }else if(!TextUtils.isEmpty(Request.defaultWxAppid)){
                    request.setWxappid(Request.defaultWxAppid);
                }
                if (!TextUtils.isEmpty(vericode)) {
                    request.setVericode(vericode);
                }
                ApiConnect conn = new ApiConnect();
                if (args != null) {
                    if (args.length > 0) {
                        Object o = args[0];
                        if (o instanceof Callback) {
                            conn.setCallback((Callback) o);
                            if (args.length > 1 && args[1] instanceof Class) {
                                conn.setItemClass((Class<?>) args[1]);
                            }
                        } else if (o instanceof Class) {
                            conn.setItemClass((Class<?>) o);
                        } else {
                            request.setRequestParam(o);
                            if (args.length > 1) {
                                if (args[1] instanceof Class) {
                                    conn.setItemClass((Class<?>) args[1]);
                                } else {
                                    conn.setCallback((Callback) args[1]);
                                }
                            }
                            if (args.length > 2 && args[2] instanceof Class) {
                                conn.setItemClass((Class<?>) args[2]);
                            }
                        }
                    }
                }
                conn.setRequest(request);
                conn.setOffset(offset);
                conn.setSize(size);
                return conn;
            }
        });
    }
}
