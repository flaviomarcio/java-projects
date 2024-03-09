package br.com.business.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Configuration
public class ObjectUtil {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private Object target = null;
    private List<String> propertiesExcluded = null;
    private List<String> propertiesOnly = null;

    public ObjectUtil() {

    }

    public ObjectUtil(Object target) {
        this.target = target;
    }

    @Bean
    public static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static String toJson(Object o) {
        if (o == null)
            return null;
        var objectMapper = newObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Map<String, String> toMap(Object o) {
        if (o == null)
            return null;
        Map<String, String> fieldValues = new HashMap<>();
        Field[] fieldList = o.getClass().getDeclaredFields();
        for (Field field : fieldList) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue;
            try {
                Object objectValue = field.get(o);
                fieldValue = (objectValue != null)
                        ? objectValue.toString()
                        : null;
            } catch (IllegalAccessException e) {
                fieldValue = null;
            }
            fieldValue = (fieldValue == null) ? "" : fieldValue;
            fieldValues.put(fieldName, fieldValue);
        }
        return fieldValues;
    }

    public static JsonNode toJsonObject(String json) {
        if (json == null || json.isEmpty())
            return null;
        var objectMapper = newObjectMapper();
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        if (json == null || json.isEmpty())
            return null;
        var objectMapper = newObjectMapper();
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isUuid(String value) {
        if (value == null || value.trim().isEmpty())
            return false;
        return UUID_REGEX.matcher(value).matches();
    }

    public static UUID toUuid(String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static UUID toUuid(Object o) {
        return toUuid(toJson(o));
    }

    public static UUID toMd5Uuid(String value) {
        if (value == null || value.isEmpty())
            return null;

        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            return UUID.nameUUIDFromBytes(value.getBytes());
        }
    }

    public static UUID toMd5Uuid(Object o) {
        return toMd5Uuid(toJson(o));
    }

    public static String toMd5(String value) {
        var uuid = toMd5Uuid(value);
        return uuid == null ? "" : uuid.toString();
    }

    public static String toMd5(Object o) {
        return toMd5(toJson(o));
    }

    public Object getTarget() {
        return target;
    }

    public ObjectUtil setTarget(Object target) {
        this.target = target;
        return this;
    }

    public ObjectUtil reset() {
        target = null;
        propertiesExcluded.clear();
        propertiesOnly.clear();
        return this;
    }

    public ObjectUtil addPropertiesExcluded(String propertyName) {
        if (propertiesExcluded == null)
            propertiesExcluded = new ArrayList<>();
        if (!propertiesExcluded.contains(propertyName))
            propertiesExcluded.add(propertyName.toLowerCase());
        return this;
    }

    public ObjectUtil addPropertiesOnly(String propertyName) {
        if (propertiesOnly == null)
            propertiesOnly = new ArrayList<>();
        if (!propertiesOnly.contains(propertyName))
            propertiesOnly.add(propertyName.toLowerCase());
        return this;
    }

}
