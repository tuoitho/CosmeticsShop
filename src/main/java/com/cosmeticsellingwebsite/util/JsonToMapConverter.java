// JsonToMapConverter.java
package com.cosmeticsellingwebsite.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Fallback to empty JSON object if serialization fails
        }
    }

//    @Override
//    public Map<String, Object> convertToEntityAttribute(String dbData) {
//        try {
//            return objectMapper.readValue(dbData, Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new HashMap<>(); // Fallback to empty map if deserialization fails
//        }
//    }
    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()){
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
