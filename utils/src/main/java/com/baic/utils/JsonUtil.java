package com.baic.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by baic on 16/4/1.
 */
public class JsonUtil {

    private JsonUtil() {

    }

    private static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static JavaType getGenericType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static JsonNode objectToJson(Object object) {
        try {
            return objectMapper.readTree(objectToString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objectToString(Object object) {
        StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, object);
            stringWriter.close();
            return stringWriter.toString();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T stringToObject(String str, Class<?> rootClasses, Class<?>... elementClasses) {
        try {
            return objectMapper.readValue(str, getGenericType(rootClasses, elementClasses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObject(JsonNode json, Class<?> rootClasses, Class<?>... elementClasses) {
        try {
            return objectMapper.readValue(json.traverse(), getGenericType(rootClasses, elementClasses));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T objectToObject(Object object, Class<?> rootClasses, Class<?>... elementClasses) {
        return jsonToObject(objectToJson(object), rootClasses, elementClasses);
    }

    public static JsonNode stringToJson(String str) {
        try {
            return objectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringValue(JsonNode json, String key) {
        if (json == null) {
            return null;
        }
        return json.get(key).textValue();
    }

    public static int getIntValue(JsonNode json, String key, int defaultInt) {
        if (json == null) {
            return defaultInt;
        }
        Integer result = json.get(key).intValue();
        return result == null ? defaultInt : result;
    }

    public static Long getLongValue(JsonNode json, String key, Long defaultInt) {
        if (json == null) {
            return defaultInt;
        }
        Long result = json.get(key).longValue();
        return result == null ? defaultInt : result;
    }
}
