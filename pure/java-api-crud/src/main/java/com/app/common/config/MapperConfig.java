package com.app.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
public class MapperConfig {
    private static ApplicationContext STATIC_CONTEXT;
    private static Environment STATIC_ENVIRONMENT;

    public MapperConfig(ApplicationContext applicationContext, Environment environment) {
        setApplicationContext(applicationContext);
        setEnvironment(environment);
    }

    public static boolean isConfigured() {
        return STATIC_CONTEXT != null && STATIC_ENVIRONMENT != null;
    }

    public static ApplicationContext getApplicationContext() {
        return getApplicationContext(MapperConfig.class);
    }

    public static void setApplicationContext(ApplicationContext context) {
        STATIC_CONTEXT = context;
    }

    public static ApplicationContext getApplicationContext(Class<?> aClass) {
        if (STATIC_CONTEXT == null) {
            var msg = String.format("Use to configure context %s.setApplicationContext(...)", MapperConfig.class);
            log.error(msg);
            throw new RuntimeException(String.format("Invalid context: %s", aClass));
        }
        return STATIC_CONTEXT;
    }

    public static Environment getEnvironment() {
        return getEnvironment(MapperConfig.class);
    }

    public static void setEnvironment(Environment environment) {
        STATIC_ENVIRONMENT = environment;
    }

    public static Environment getEnvironment(Class<?> aClass) {
        if (STATIC_ENVIRONMENT == null) {
            var msg = String.format("Use to configure context %s.setEnvironment(...)", MapperConfig.class);
            log.error(msg);
            throw new RuntimeException(String.format("Invalid environment: %s", aClass));
        }
        return STATIC_ENVIRONMENT;
    }

    public static ObjectMapper newObjectMapper(Map<SerializationFeature, Boolean> serialization, Map<DeserializationFeature, Boolean> deserialization) {
        var mapper = new ObjectMapper(new JsonFactory());
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(ObjectConverter.modules());

        serialization.forEach(mapper::configure);
        deserialization.forEach(mapper::configure);

        return mapper;
    }

    @Bean
    @ConditionalOnMissingBean()
    public static ObjectMapper newObjectMapper() {
        return newObjectMapper(
                Map.of(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true),
                Map.of(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
    }

    public static ModelMapper newModelMapper() {
        var mapper = new ModelMapper();
        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        ObjectConverter
                .converters()
                .forEach(mapper::addConverter);
        return mapper;
    }

    @Bean
    @ConditionalOnMissingBean()
    public ApplicationContext applicationContext() {
        return this.STATIC_CONTEXT;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ObjectConverter {

        private static final String FORMAT_DATE = "yyyy-MM-dd";
        private static final String FORMAT_TIME = "HH:mm:ss";
        private static final String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        private static final Converter<String, LocalDate> toLocalDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                if (Objects.nonNull(source) && !source.trim().isEmpty())
                    return LocalDate.parse(source, DateTimeFormatter.ofPattern(FORMAT_DATE));
                return null;
            }
        };
        private static final Converter<LocalDate, String> toLocalDateString = new AbstractConverter<LocalDate, String>() {
            @Override
            protected String convert(LocalDate source) {
                if (Objects.nonNull(source))
                    return source.format(DateTimeFormatter.ofPattern(FORMAT_DATE));
                return null;
            }
        };
        private static final Converter<String, LocalTime> toLocalTime = new AbstractConverter<String, LocalTime>() {
            @Override
            protected LocalTime convert(String source) {
                if (Objects.nonNull(source) && !source.trim().isEmpty())
                    return LocalTime.parse(source, DateTimeFormatter.ofPattern(FORMAT_TIME));
                return null;
            }
        };
        private static final Converter<LocalTime, String> toLocalTimeString = new AbstractConverter<LocalTime, String>() {
            @Override
            protected String convert(LocalTime source) {
                if (Objects.nonNull(source))
                    return source.format(DateTimeFormatter.ofPattern(FORMAT_TIME));
                return null;
            }
        };
        private static final Converter<String, LocalDateTime> toLocalDateTime = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String source) {
                if (Objects.nonNull(source) && !source.trim().isEmpty())
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(FORMAT_DATETIME));
                return null;
            }
        };
        private static final Converter<LocalDateTime, String> toLocalDateTimeString = new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime source) {
                if (Objects.nonNull(source))
                    return source.format(DateTimeFormatter.ofPattern(FORMAT_DATETIME));
                return null;
            }
        };

        public static SimpleModule modules() {
            SimpleModule modules = new SimpleModule();
            modules.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_DATE)));
                }
            });
            modules.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_TIME)));
                }
            });
            modules.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_DATETIME)));
                }
            });
            return modules;
        }

        public static List<Converter<?, ?>> converters() {
            return List.of(
                    toLocalDate, toLocalDateString,
                    toLocalTime, toLocalTimeString,
                    toLocalDateTime, toLocalDateTimeString
            );
        }

    }

}
