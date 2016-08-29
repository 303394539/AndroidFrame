package com.baic.cache.simplecache;

import android.content.Context;

import com.baic.utils.JsonUtil;
import com.baic.utils.StringUtil;

/**
 * Created by baic on 16/7/8.
 */
public class Cache {

    private ACache aCache;

    private Cache(Context context){
        aCache = ACache.get(context);
    }

    public static Cache create(Context context){
        return new Cache(context);
    }

    public void save(String key, String value, int second){
        aCache.put(key, value, second);
    }

    public void save(String key, String value){
        aCache.put(key, value);
    }

    public String get(String key){
        return aCache.getAsString(key);
    }

    public void saveObj(String key, Object object, int second){
        save(key, JsonUtil.objectToString(object), second);
    }

    public void saveObj(String key, Object object){
        save(key, JsonUtil.objectToString(object));
    }

    public <T> T getObj(String key, Class<?> rootClasses, Class<?>... elementClasses){
        String result = get(key);
        if(!StringUtil.isNullOrEmpty(result)){
            return JsonUtil.stringToObject(result, rootClasses, elementClasses);
        }
        return null;
    }

    public void remove(String key){
        aCache.remove(key);
    }

    public void clear(){
        aCache.clear();
    }
}
