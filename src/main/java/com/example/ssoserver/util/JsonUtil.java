package com.example.ssoserver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author: lichaoyang
 * @Date: 2019-09-09 01:48
 */
public class JsonUtil {

    private JsonUtil() {
    }

    public static <T> T json2Object(final String json, final Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            try {
                JsonMapper mapper = new JsonMapper();
                return mapper.readValue(json, clazz);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static <T> T json2Object(String json, TypeReference<T> typeReference) {
        try {
            JsonMapper mapper = new JsonMapper();
            return mapper.readValue(json, typeReference);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> json2Map(final String json) {
        return (Map)json2Object(json, Map.class);
    }

    public static List<Object> json2List(final String json) {
        return (List)json2Object(json, List.class);
    }

    public static String object2Json(final Object object) {
        if (object == null) {
            return null;
        } else {
            JsonMapper mapper = new JsonMapper();

            try {
                return mapper.writeValueAsString(object);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static class JsonMapper extends ObjectMapper {
        private static final long serialVersionUID = 1L;
        static ObjectMapper mapper = null;

        public JsonMapper() {
            super(mapper);
        }

        static {
            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }
}
