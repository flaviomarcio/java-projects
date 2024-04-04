package com.app.common.util;

import com.app.common.config.MapperConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    public static String classToName(Object o) {
        if (o == null)
            return "";
        if (o.getClass().equals(String.class))
            return (String) o;
        if (o.getClass().equals(Class.class)) {
            var aClass = ((Class<?>) o);
            return aClass.getName();
        }
        if (o.getClass().isEnum())
            return o.toString();
        return "";
    }

    @SuppressWarnings("unused")
    private static List<Class<?>> getClassesBySorted(Set<Class<?>> classes) {
        if (classes == null || classes.isEmpty())
            return new ArrayList<>();
        List<Class<?>> __return = new ArrayList<>(classes);
        __return
                .sort(new Comparator<Class<?>>() {
                    @Override
                    public int compare(Class<?> aClass1, Class<?> aClass2) {
                        return aClass1.getName().toLowerCase().compareTo(aClass2.getName().toLowerCase());
                    }
                });
        return new ArrayList<>();
    }

    public static Class<?> getClassByName(Object classType) {
        var className = classToName(classType);
        if (className.isEmpty())
            return null;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static List<Class<?>> getClassesByInherits(Class<?> classType) {
        if (classType == null)
            return new ArrayList<>();
        return new ArrayList<>();
    }

    public static List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotationClass) {
        return getClassesByInherits(annotationClass);
    }

    public static void update(Object object, Object newValues) {
        if (object == null)
            throw new RuntimeException("Invalid object");

        if (newValues == null)
            throw new RuntimeException("Invalid new values");

        try {
            var updateBytes = IOUtil.readAll(newValues).trim();
            var updateValues = updateBytes.isEmpty()
                    ? newValues
                    : ObjectUtil.createFromString(object.getClass(), updateBytes);
            var objectMapper = MapperConfig.newObjectMapper();
            objectMapper.updateValue(object, updateValues);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized Field toFieldByName(Class<?> tClass, String fieldName) {
        if (fieldName == null || tClass == null)
            return null;
        fieldName = fieldName.trim().toLowerCase();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.getName().toLowerCase().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static synchronized Field toFieldByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass == null || annotationClass == null)
            return null;
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList)
            if (field.isAnnotationPresent(annotationClass))
                return field;
        return null;
    }

    public static synchronized List<Field> toFieldsByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass == null || annotationClass == null)
            return null;
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList)
            if (field.isAnnotationPresent(annotationClass))
                __return.add(field);
        return __return;
    }

    public static synchronized Field toFieldByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass == null || typeClass == null)
            return null;
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.getType().equals(typeClass))
                return field;
        }
        return null;
    }

    public static synchronized List<Field> toFieldsByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass == null || typeClass == null)
            return null;
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.getType().equals(typeClass))
                __return.add(field);
        }
        return __return;
    }

    public static synchronized <T> List<Field> toFieldsList(Class<T> tClass) {
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            field.setAccessible(true);
            __return.add(field);
        }
        return __return;
    }

    public static synchronized List<Field> toFieldsList(Object o) {
        if (o == null)
            return new ArrayList<>();
        if (PRIMITIVE_CLASSES.contains(o.getClass().getName()))
            return new ArrayList<>();
        return toFieldsList(o.getClass());
    }

    public static synchronized Map<String, Field> toFieldsMap(Class<?> tClass) {
        Map<String, Field> __return = new HashMap<>();
        toFieldsList(tClass)
                .forEach(field -> __return.put(field.getName(), field));
        return __return;
    }

    public static synchronized Map<String, Field> toFieldsMap(final Object o) {
        if (o == null)
            return new HashMap<>();
        return toFieldsMap(o.getClass());
    }

    public static synchronized boolean equal(final Object a, final Object b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;
        var md5A = toMd5Uuid(a);
        var md5B = toMd5Uuid(b);
        if (md5A == md5B)
            return true;
        if (md5A == null || md5B == null)
            return false;
        return md5A.toString().equals(md5B.toString());
    }

    public static <T> T create(Class<?> classType) {
        try {
            var c = classType.getConstructor();
            c.setAccessible(true);
            //noinspection unchecked
            return (T) c.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static <T> T createWithArgsConstructor(Class<?> aClass, Object... initArgs) {
        if (initArgs == null || initArgs.length == 0)
            return create(aClass);
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

                if (constructor == null)
                    continue;
                constructor.setAccessible(true);
                //noinspection unchecked
                return (T) constructor.newInstance(initArgs);
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }

    public static <T> T createFromString(Class<T> aClass, String src) {
        var mapper = MapperConfig.newObjectMapper();
        try {
            return mapper.readValue(src, aClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    public static <T> T createFromFile(Class<T> aClass, File file) {
        try {
            return createFromStream(aClass, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static <T> T createFromStream(Class<T> aClass, InputStream stream) {
        try {
            return createFromString(aClass, new String(stream.readAllBytes()));
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T createFromJSON(Class<T> claClass, String values) {
        return createFromString(claClass, values);
    }

    public static <T> T createFromValues(Class<T> aClass, final Map<String, Object> srcValues) {
        if (aClass == null) {
            return null;
        }
        if (srcValues == null || srcValues.isEmpty()) {
            log.debug("No source values to write class [{}]", aClass.getName());
            return null;
        }
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

        srcValues.forEach((fieldName, fieldValue) -> {
            try {
                var fieldWrite = fieldsWriter.get(fieldName.trim().toLowerCase());
                if (fieldWrite == null)
                    return;
                finaMapValues.put(fieldWrite.getName(), fieldValue);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return MapperConfig.newModelMapper().map(finaMapValues, aClass);
    }

    public static <T> T createFromObject(Class<T> aClass, Object objectSrc) {
        if (aClass == null) {
            return null;
        }
        if (objectSrc == null) {
            log.debug("No object source to write class [{}]", aClass.getName());
            return null;
        }
        var objectValues = toMapObject(objectSrc);
        if (objectValues.isEmpty())
            return null;
        return createFromValues(aClass, objectValues);
    }

    public static String toString(Object o) {
        if (o == null)
            return "";
        if (o.getClass().equals(String.class))
            return (String) o;
        try {
            var mapper = MapperConfig.newObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Deprecated(since = "use function ObjectUtil.toString")
    public static String toJson(Object o) {
        return toString(o);
    }

    public static synchronized Map<String, Object> toMapObject(final Object o) {
        if (o == null)
            return new HashMap<>();

        if (o.getClass().equals(String.class)) {
            Map<String, Object> fieldValues = new HashMap<>();
            var mapValues = toMapOfString(o.toString());
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            fieldValues.putAll(mapValues);
            return fieldValues;
        } else if (PRIMITIVE_CLASSES.contains(o.getClass().getName())) {
            return new HashMap<>();
        } else {
            Map<String, Object> fieldValues = new HashMap<>();
            toFieldsList(o.getClass())
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            fieldValues.put(field.getName(), field.get(o));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return fieldValues;
        }
    }

    public static synchronized Map<String, String> toMapOfString(final Object o) {
        if (o == null)
            return new HashMap<>();

        if (o.getClass().equals(String.class)) {
            try {
                var mapper = MapperConfig.newObjectMapper();
                return mapper.readValue((String) o, Map.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
            return new HashMap<>();
        }


        Map<String, String> fieldValues = new HashMap<>();
        toFieldsList(o.getClass())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        var oGet = field.get(o);
                        if (oGet == null) {
                            fieldValues.put(field.getName(), "");
                            return;
                        }

                        if (oGet.getClass().isPrimitive() || oGet.getClass().isEnum() || PRIMITIVE_CLASSES.contains(field.getGenericType().getTypeName())) {
                            fieldValues.put(field.getName(), oGet.toString());
                            return;
                        }

                        if (oGet.getClass().isLocalClass())
                            fieldValues.put(field.getName(), toString(oGet));

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return fieldValues;
    }


    public static String toMd5(Object o) {
        return HashUtil.toMd5(o);
    }

    public static UUID toMd5Uuid(Object o) {
        return HashUtil.toMd5Uuid(o);
    }

}
