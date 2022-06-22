package com.example.usermanagement.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectToJson {
    private static final Logger log = LoggerFactory.getLogger(ObjectToJson.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> String convert(T o) {

        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            log.warn("Error while converting object to json", ex);
            return "JSON_ERR";
        }
    }
}
