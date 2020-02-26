package org.apache.servicecomb.tracing.zipkin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object model) {
        try {
            if(model == null)
            {
                return null;
            }
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
//            logger.error("POJO to json string error", e);
            logger.error("POJO to json string error");
        }
        return null;
    }

    public static <T> T parseJsonString(String jsonStr, Class<T> clazz) {
        try {
            return mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
//            logger.error("json string to POJO error", e);
            logger.error("json string to POJO error");
        }
        return null;
    }

    public static JsonNode parseJsonString(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (IOException e) {
            logger.error("Unable to parse json string");
        }
        return null;
    }
}
