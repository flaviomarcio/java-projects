package br.com.core.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${springdoc.servers.dev:}")
    private String urlDevServer;
    @Value("${springdoc.servers.staging:}")
    private String urlStageServer;
    @Value("${springdoc.servers.prod:}")
    private String urlProdServer;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("SRO Insurance - API de Gerenciamento da Stack no Orquestrador")
                        .description("Microsserviço de gerenciamento da stack no orquestrador.")
                        .version("0.0.1"))
                .servers(configServers());
    }

    private List<Server> configServers() {
        return Arrays.asList(new Server().url(urlDevServer),
                new Server().url(urlStageServer),
                new Server().url(urlProdServer));
    }
}
