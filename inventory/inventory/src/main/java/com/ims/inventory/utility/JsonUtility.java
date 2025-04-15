package com.ims.inventory.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonUtility {

    public static Map<String, Object> jsonStrToMap(String str) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(str, new TypeReference<HashMap<String,Object>>() {});
        } catch(JsonProcessingException  e){
            log.error("JsonUtility :: jsonStringToMap() :: Exception while JSON parsing.", e);
            throw e;
        }
    }

}
