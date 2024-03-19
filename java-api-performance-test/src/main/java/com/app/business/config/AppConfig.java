package com.app.business.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Data
@Builder
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final Parallelism parallelism;
    private final Target target;
    private final Body body;



    @Data
    @Builder
    @Configuration
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parallelism {
        @Value("${app.parallelism.thread-pool:1}")
        private int threadPool;
        @Value("${app.parallelism.thread-count:1}")
        private int threadCount;
        @Value("${app.parallelism.try-count:1}")
        private int tryCount;

        @Value("${app.parallelism.timeout-secs:1}")
        private int timeoutSecs;

        public int getThreadCount(){
            return this.threadCount <=0?1:this.threadCount;
        }

        public int getTryCount(){
            return this.tryCount<=0?1:this.tryCount;
        }
    }

    @Data
    @Builder
    @Configuration
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Target {
        @Value("${app.method:POST}")
        private Method method;
        @Value("${app.uri:http://localhost:8080}")
        private URI uri;
    }

    @Data
    @Builder
    @Configuration
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Body {
        @Value("${app.body.type:auto}")
        private SourceType type;
        @Value("${app.body.size-kb:100}")
        private int sizeKb;
        @Value("${app.body.source:}")
        private String source;
    }

    @Getter
    public enum SourceType{
        AUTO("auto");
        @JsonValue
        private final String value;
        SourceType(String value){
            this.value=value;
        }
    }

    public enum Method{
        GET,POST,PUT,DELETE
    }
}
