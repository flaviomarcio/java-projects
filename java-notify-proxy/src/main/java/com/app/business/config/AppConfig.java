package com.app.business.config;

import com.app.business.domain.InputType;
import com.app.business.domain.NodeType;
import com.littlecode.util.EnvironmentUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Data
@Configuration
public class AppConfig {
    private final Node node;
    private final Input input;

    public AppConfig(Environment env) {
        var envUtil = new EnvironmentUtil(env);
        this.node = Node
                .builder()
                .type(envUtil.asEnum("config.host.type", NodeType.class, NodeType.MASTER))
                .host(
                        Host
                                .builder()
                                .address(envUtil.asString("config.host.address", "localhost"))
                                .port(envUtil.asInt("config.host.port", 8080))
                                .token(envUtil.asString("config.host.token"))
                                .build()
                )
                .build();

        this.input = Input
                .builder()
                .type(envUtil.asEnum("config.input.type", InputType.class, InputType.None))
                .queue(
                        Queue
                                .builder()
                                .name(envUtil.asString("config.input.queue.name"))
                                .credential(
                                        Credential
                                                .builder()
                                                .id(envUtil.asString("config.input.queue.credential.id"))
                                                .secret(envUtil.asString("config.input.queue.credential.secret"))
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {
        private InputType type;
        private Queue queue;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Queue {
        private String name;
        private Credential credential;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Credential {
        private String id;
        private String secret;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Host {
        private String address;
        private int port;
        private String token;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private NodeType type;
        private Host host;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SlackSetting {
        private String token;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Static {
        private SlackSetting slack;
    }
}
