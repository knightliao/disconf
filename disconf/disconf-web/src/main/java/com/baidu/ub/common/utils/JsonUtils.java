package com.baidu.ub.common.utils;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;

import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * JSON工具类
 * 
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-4-17 下午4:25:22
 */
public class JsonUtils {

    private static final Logger log = AopLogFactory.getLogger(JsonUtils.class);

    final static ObjectMapper objectMapper;

    public static final class IosDateTimeJsonSerializer extends
            JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator paramJsonGenerator,
                SerializerProvider provider) throws IOException,
                JsonProcessingException {
            if (value != null) {
                paramJsonGenerator.writeString(DateUtils.ISO_DATETIME_FORMAT
                        .format(value));
            }
        }

    };

    static {
        StdSerializerProvider sp = new StdSerializerProvider();
        // sp.setNullValueSerializer(new NullNullSerializer());
        objectMapper = new ObjectMapper(null, sp, null);
        // objectMapper.setDateFormat(new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     * 
     * @param <T>
     * @param jsonString
     *            JSON字符串
     * @param tr
     *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2GenericObject(String jsonString,
            TypeReference<T> tr) {

        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return (T) objectMapper.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Java对象转Json字符串
     */
    public static String toJson(Object object) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage());
        }
        return jsonString;

    }

    /**
     * Json字符串转Java对象
     */
    public static Object json2Object(String jsonString, Class<?> c) {

        if (jsonString == null || "".equals(jsonString)) {
            return "";
        } else {
            try {
                return objectMapper.readValue(jsonString, c);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }

        }
        return "";
    }

}
