package com.app.common.util;

import com.app.common.config.MapperConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ObjectUtil {

    private static final List<String> PRIMITIVE_CLASSES = new ArrayList<>();


    static {
        PRIMITIVE_CLASSES.add(String.class.getName());
        PRIMITIVE_CLASSES.add(Byte.class.getName());
        PRIMITIVE_CLASSES.add(UUID.class.getName());
        PRIMITIVE_CLASSES.add(Boolean.class.getName());
        PRIMITIVE_CLASSES.add(Integer.class.getName());
        PRIMITIVE_CLASSES.add(Long.class.getName());
        PRIMITIVE_CLASSES.add(Double.class.getName());
        PRIMITIVE_CLASSES.add(BigDecimal.class.getName());
        PRIMITIVE_CLASSES.add(BigInteger.class.getName());
        PRIMITIVE_CLASSES.add(LocalDate.class.getName());
        PRIMITIVE_CLASSES.add(LocalTime.class.getName());
        PRIMITIVE_CLASSES.add(LocalDateTime.class.getName());
        PRIMITIVE_CLASSES.add("java.lang.Class<?>");
    }

    @Synchronized
    public static Field toFieldByName(Class<?> tClass, String fieldName) {
        if (tClass != null && fieldName != null){
            fieldName = fieldName.trim().toLowerCase();
            if(!fieldName.isEmpty()){
                Field[] fieldList = tClass.getDeclaredFields();
                for (Field field : fieldList) {
                    if (field.getName().toLowerCase().equals(fieldName)) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            }
        }
        return null;
    }

    @Synchronized
    public static Field toFieldByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass != null && annotationClass != null){
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList)
                if (field.isAnnotationPresent(annotationClass))
                    return field;
        }
        return null;
    }

    @Synchronized
    public static List<Field> toFieldsByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass != null && annotationClass != null){
            ArrayList<Field> __return = new ArrayList<>();
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList)
                if (field.isAnnotationPresent(annotationClass))
                    __return.add(field);
            return __return;
        }
        return new ArrayList<>();
    }

    @Synchronized
    public static Field toFieldByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass != null && typeClass != null){
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList) {
                if (field!= null && field.getType().equals(typeClass))
                    return field;
            }
        }
        return null;
    }

    @Synchronized
    public static List<Field> toFieldsByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass == null || typeClass == null)
            return null;
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field!= null && field.getType().equals(typeClass))
                __return.add(field);
        }
        return __return;
    }

    public static synchronized <T> List<Field> toFieldsList(Class<T> tClass) {
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field!= null && !Modifier.isStatic(field.getModifiers())){
                field.setAccessible(true);
                __return.add(field);
            }
        }
        return __return;
    }

    public static synchronized List<Field> toFieldsList(Object o) {
        if (o == null)
            return new ArrayList<>();
        if (!PRIMITIVE_CLASSES.contains(o.getClass().getName()))
            return toFieldsList(o.getClass());
        return new ArrayList<>();
    }

    @Synchronized
    public static Map<String, Field> toFieldsMap(Class<?> tClass) {
        HashMap<String, Field> __return = new HashMap<>();
        var list=toFieldsList(tClass);
        for (Field field : list) {
            if(field!=null)
                __return.put(field.getName(), field);
        }
        return Collections.unmodifiableMap(__return);
    }

    public static synchronized Map<String, Field> toFieldsMap(final Object o) {
        if (o != null)
            return toFieldsMap(o.getClass());
        return new HashMap<>();
    }

    public static synchronized boolean equal(final Object a, final Object b) {
        if (a == b)
            return true;
        else if (a != null && b != null){
            var md5A = toMd5Uuid(a);
            var md5B = toMd5Uuid(b);
            if (md5A == md5B)
                return true;
            if (md5A != null && md5B != null)
                return md5A.toString().equals(md5B.toString());
        }
        return false;
    }

    public static <T> T create(Class<?> classType) {
        try {
            var c = classType.getConstructor();
            c.setAccessible(true);
            //noinspection unchecked
            return (T) c.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |  IllegalAccessException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static <T> T createWithArgsConstructor(Class<?> aClass, Object... initArgs) {
        if (initArgs != null && initArgs.length != 0){
            try {
                for (var constructor : aClass.getConstructors()) {
                    final var types = constructor.getParameterTypes();
                    if (types.length != initArgs.length)
                        continue;
                    int iArg = 0;
                    for (var type : types) {
                        var arg = initArgs[iArg++];
                        if (!arg.getClass().equals(type)) {
                            constructor = null;
                            break;
                        }
                    }

                    if (constructor != null) {
                        constructor.setAccessible(true);
                        //noinspection unchecked
                        return (T) constructor.newInstance(initArgs);
                    }
                }

            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
            return null;
        }
        return create(aClass);
    }

    public static <T> T createFromString(Class<T> aClass, String src) {
        AtomicReference<ObjectMapper> mapper = new AtomicReference<>(MapperConfig.newObjectMapper());
        try {
            return mapper.get().readValue(src, aClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    public static <T> T createFromFile(Class<T> aClass, File file) {
        if(aClass!=null && file!=null){
            try {
                return createFromStream(aClass, new FileInputStream(file));
            } catch (FileNotFoundException ignore) {}
        }
        return null;
    }

    public static <T> T createFromStream(Class<T> aClass, InputStream stream) {
        try {
            return createFromString(aClass, new String(stream.readAllBytes()));
        } catch (IOException ignore) {}
        return null;
    }

    public static <T> T createFromValues(Class<T> aClass, final Map<?, ?> srcValues) {
        if (aClass != null && srcValues != null && !srcValues.isEmpty()) {
           var fieldsNew = toFieldsList(aClass);
            if (fieldsNew.isEmpty()) {
                log.debug("No fields from [{}]", aClass.getName());
                return null;
            }

            Map<String, Field> fieldsWriter = new HashMap<>();
            fieldsNew.forEach(field -> fieldsWriter.put(field.getName().toLowerCase(), field));
            if (fieldsWriter.isEmpty()) {
                log.debug("No fields to write from class [{}]", aClass.getName());
                return null;
            }

            Map<String, Object> finaMapValues = new HashMap<>();

            for (Map.Entry<?, ?> entry : srcValues.entrySet()) {
                String fieldName = entry.getKey().toString();
                Object fieldValue = entry.getValue();
                try {
                    var fieldWrite = fieldsWriter.get(fieldName.trim().toLowerCase());
                    if (fieldWrite == null)
                        continue;
                    finaMapValues.put(fieldWrite.getName(), fieldValue);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            return MapperConfig.newModelMapper().map(finaMapValues, aClass);
        }
        return null;

    }

    public static <T> T createFromObject(Class<T> aClass, Object objectSrc) {
        if (aClass != null && objectSrc != null) {
            var objectValues = toMapObject(objectSrc);
            if (!objectValues.isEmpty())
                return createFromValues(aClass, objectValues);
        }
        return null;
    }

    public static String toString(Object o) {
        if (o != null){
            if (!o.getClass().equals(String.class)) {
                try {
                    var mapper = MapperConfig.newObjectMapper();
                    return mapper.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
            }
            else{
                return (String) o;
            }
        }
        return "";
    }

    public static synchronized Map<?, ?> toMapObject(final Object o) {
        if (o != null){
            if (o.getClass().equals(String.class)) {
                return toMapOfString(o.toString());
            }
            else if (!PRIMITIVE_CLASSES.contains(o.getClass().getName())) {
                Map<String, Object> fieldValues = new HashMap<>();
                var list=toFieldsList(o.getClass());
                for (Field field : list) {
                    if(field!=null){
                        field.setAccessible(true);
                        try {
                            fieldValues.put(field.getName(), field.get(o));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return fieldValues;
            }
        }
        return new HashMap<>();
    }

    public static synchronized Map<?,?> toMapOfString(final Object o) {
        if (o != null){
            if (o.getClass().equals(String.class)) {
                try {
                    var mapper = MapperConfig.newObjectMapper();
                    return mapper.readValue((String) o, Map.class);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
            }
            else{
                Map<String, String> fieldValues = new HashMap<>();
                for (Field field : toFieldsList(o.getClass())) {
                    field.setAccessible(true);
                    try {
                        var oGet = field.get(o);
                        if (oGet == null) {
                            fieldValues.put(field.getName(), "");
                            continue;
                        }

                        if (oGet.getClass().isPrimitive() || oGet.getClass().isEnum() || PRIMITIVE_CLASSES.contains(field.getGenericType().getTypeName())) {
                            fieldValues.put(field.getName(), oGet.toString());
                            continue;
                        }

                        if (oGet.getClass().isLocalClass())
                            fieldValues.put(field.getName(), toString(oGet));

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                return fieldValues;
            }

        }
        return new HashMap<>();
    }


    public static String toMd5(Object o) {
        return HashUtil.toMd5(o);
    }

    public static UUID toMd5Uuid(Object o) {
        return HashUtil.toMd5Uuid(o);
    }

}
