package com.baic.net.api;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by baic on 16/7/7.
 */
public class JniUtils {

    public static native String getSign(Context context, String openid, String target);
    public static native String getResult(Context context, String openid, String target);

    static {
        try {
            System.loadLibrary("NetNdkJni");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
