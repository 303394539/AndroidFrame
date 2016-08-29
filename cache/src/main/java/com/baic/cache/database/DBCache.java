package com.baic.cache.database;

import android.content.Context;

import com.baic.utils.JsonUtil;
import com.baic.utils.StringUtil;

import java.util.List;

/**
 * Created by baic on 16/7/10.
 */
public class DBCache {

    public static void save(String key, String value, long time) {
        if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
            DBManager.save(key, value, time);
        }
    }

    public static void save(String key, String value) {
        if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
            DBManager.save(key, value);
        }
    }

    public static void saveObj(String key, Object obj, long time) {
        if (!StringUtil.isNullOrEmpty(key) && obj != null) {
            save(key, JsonUtil.objectToString(obj), time);
        }
    }

    public static void saveObj(String key, Object obj) {
        if (!StringUtil.isNullOrEmpty(key) && obj != null) {
            save(key, JsonUtil.objectToString(obj));
        }
    }

    public static String get(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            Data data = DBManager.get(key);
            if (data != null && !StringUtil.isNullOrEmpty(data.getContent())) {
                return data.getContent();
            }
        }
        return null;
    }

    public static <T> T getObj(String key, Class<?> rootClasses, Class<?>... elementClasses) {
        if (!StringUtil.isNullOrEmpty(key)) {
            String result = get(key);
            if(!StringUtil.isNullOrEmpty(result)){
                return JsonUtil.stringToObject(result, rootClasses, elementClasses);
            }
        }
        return null;
    }

    public static <T> T getObjList(String key, Class<?> classes) {
        if (!StringUtil.isNullOrEmpty(key)) {
            String result = get(key);
            if(!StringUtil.isNullOrEmpty(result)){
                return JsonUtil.stringToObject(result, List.class, classes);
            }
        }
        return null;
    }

    public static void saveTmp(String key, String value, long time) {
        if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
            DBManager.saveTmp(key, value, time);
        }
    }

    public static void saveTmp(String key, String value) {
        if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
            DBManager.saveTmp(key, value);
        }
    }

    public static void saveTmpObj(String key, Object obj, long time) {
        if (!StringUtil.isNullOrEmpty(key) && obj != null) {
            saveTmp(key, JsonUtil.objectToString(obj), time);
        }
    }

    public static void saveTmpObj(String key, Object obj) {
        if (!StringUtil.isNullOrEmpty(key) && obj != null) {
            saveTmp(key, JsonUtil.objectToString(obj));
        }
    }

    public static String getTmp(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            Data data = DBManager.getTmp(key);
            if (data != null && !StringUtil.isNullOrEmpty(data.getContent())) {
                return data.getContent();
            }
        }
        return null;
    }

    public static <T> T getTmpObj(String key, Class<?> rootClasses, Class<?>... elementClasses) {
        if (!StringUtil.isNullOrEmpty(key)) {
            String result = getTmp(key);
            if(!StringUtil.isNullOrEmpty(result)){
                return JsonUtil.stringToObject(result, rootClasses, elementClasses);
            }
        }
        return null;
    }

    public static <T> T getTmpObjList(String key, Class<?> classes) {
        if (!StringUtil.isNullOrEmpty(key)) {
            String result = getTmp(key);
            if(!StringUtil.isNullOrEmpty(result)){
                return JsonUtil.stringToObject(result, List.class, classes);
            }
        }
        return null;
    }

    public static void remove(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            DBManager.remove(key);
        }
    }

    public static void removeTmp(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            DBManager.removeTmp(key);
        }
    }

    public static void clear() {
        DBManager.clear();
    }

    public static void clearTmp() {
        DBManager.clearTmp();
    }

    public static void clearAll() {
        DBManager.clear();
        DBManager.clearTmp();
    }
}
