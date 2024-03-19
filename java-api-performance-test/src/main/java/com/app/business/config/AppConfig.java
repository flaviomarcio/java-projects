package com.app.business.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

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

        @Value("${app.parallelism.timeout-secs:0}")
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
        @Value("${app.headers:}")
        private List<String> headers;

        public List<String> getHeaders() {
            if (headers == null || headers.isEmpty())
                headers = List.of("Content-Type", "application/json");
            return headers;
        }
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

        public String getBody() {
            if (this.source == null || this.source.trim().isEmpty())
                return null;
            var file = Path.of(this.source).toFile();
            if (file.exists()) {
                try (var inputStream = new FileInputStream(file)) {
                    StringBuilder str = new StringBuilder();
                    int content;
                    while ((content = inputStream.read()) != -1)
                        str.append((char) content);
                    return str.toString().trim();
                } catch (IOException e) {
                    System.out.print(e.getMessage());
                    return "";
                }
            }
            return null;
        }

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
