package com.baic.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by baic on 16/5/27.
 */
public class StringUtil {

    private static final String UTF8 = "UTF-8";

    public static MessageDigest messageDigest;

    private StringUtil() {

    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String trim(String str) {
        try {
            return str.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static int parseInt(String str, int ev) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return ev;
        }
    }

    public static int parseInt(String str) {
        return parseInt(str, -1);
    }

    public static long parseLong(String str, long d) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public static long parseLong(String str) {
        return parseLong(str, -1L);
    }

    public static double parseDouble(String str, double d) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return d;
        }
    }

    public static String formatUrl(String url, Map<String, Object> query, String prefix) {
        if (isNullOrEmpty(url)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(url);
        if (!isNullOrEmpty(prefix)) {
            if (url.indexOf(prefix) < 0) {
                sb.append(prefix);
            }
        }
        int size = query.size();
        String value = null;
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            if (entry.getValue() instanceof String
                    || entry.getValue() instanceof Integer
                    || entry.getValue() instanceof Boolean
                    || entry.getValue() instanceof Double
                    || entry.getValue() instanceof Float) {
                value = entry.getValue().toString();
            } else {
                value = JsonUtil.objectToString(entry.getValue());
            }
            sb.append(encodeURIComponent(value));
            size--;
            if (size > 0) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    public static String formatUrl(String url, Map<String, Object> query) {
        return formatUrl(url, query, "?");
    }

    //将获取到得编码进行16进制转换
    private static String bytes2Hex(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

    public static final String bytes2HexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String decodeURIComponent(String s) {
        if (StringUtil.isNullOrEmpty(s)) {
            return "";
        }
        String result = null;
        try {
            result = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

    public static String encodeURIComponent(String s) {
        if (StringUtil.isNullOrEmpty(s)) {
            return "";
        }
        String result = null;
        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

    public static String XOR(String key, String content) {
        try {
            byte[] arrK = key.getBytes(UTF8);
            byte[] arrC = content.getBytes(UTF8);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            for (int i = 0; i < arrC.length; i++) {
                byte b = arrC[i];
                byte k = arrK[i % arrK.length];
                b ^= k;
                bo.write(b);
            }
            return Base64.encodeToString(bo.toByteArray(), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5(String target) {
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        if (messageDigest == null) {
            return "";
        }
        byte[] bytes = messageDigest.digest(target.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bytes) {
            int bt = b & 0xff;
            if (bt < 16) {
                stringBuffer.append(0);
            }
            stringBuffer.append(Integer.toHexString(bt));
        }
        return stringBuffer.toString();
    }
}
