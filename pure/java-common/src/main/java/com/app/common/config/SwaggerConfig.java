package com.app.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import lombok.*;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@Getter
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String STATIC_BEARER = "Bearer ";
    private static final String STATIC_HEADER = "header";
    private final Settings settings;


    @Bean
    public OpenAPI makeOpenAPI() {
        OpenAPI servers = new OpenAPI()
                .info(
                        new Info()
                                .title(String.format("%s %s - %s", getSettings().getInfoCompany(), getSettings().getInfoProduct(), getSettings().getInfoTitle()))
                                .description(getSettings().getInfoDescription())
                                .version(getSettings().getInfoVersion())
                )
                .servers(makeServerList());
        return servers;
    }

    @Bean
    public OperationCustomizer makeDefaultHeaders() {
        OperationCustomizer operationCustomizer = (operation, handlerMethod) -> {
            operation
                    .addParametersItem(
                            new Parameter()
                                    .in(STATIC_HEADER)
                                    .name(HttpHeaders.AUTHORIZATION)
                                    .required(true)
                                    .schema(new StringSchema())
                                    .description(STATIC_BEARER + " token")
                    );

            return operation;
        };
        return operationCustomizer;
    }

    @Getter
    @Configuration
    public static class Settings {
        @Value(value = "${springdoc.info.company:}")
        private String infoCompany;

        @Value(value = "${springdoc.info.product:}")
        private String infoProduct;

        @Value(value = "${springdoc.info.title:}")
        private String infoTitle;

        @Value(value = "${springdoc.info.description:}")
        private String infoDescription;

        @Value(value = "${springdoc.info.version:v0.0.0}")
        private String infoVersion;

        @Value(value = "${springdoc.servers.dev:}")
        private String srvDev;

        @Value(value = "${springdoc.servers.stg:}")
        private String srvStg;

        @Value(value = "${springdoc.servers.prd:}")
        private String srvPrd;

        @Value(value = "${server.servlet.context-path:/}")
        private String infoContextPath;
        public Settings(){
            super();
        }
    }

    private List<Server> makeServerList() {
        List<String> servers= new ArrayList<>();
        if(getSettings().getSrvDev()!=null)
            servers.add(getSettings().getSrvDev());

        if(getSettings().getSrvStg()!=null)
            servers.add(getSettings().getSrvStg());

        if(getSettings().getSrvPrd()!=null)
            servers.add(getSettings().getSrvPrd());

        if(servers.isEmpty())
            return new ArrayList<>();

        List<Server> __return = new ArrayList<>();
        for (String dns : servers) {
            if (dns!=null && !dns.trim().isEmpty()){
                var server=new Server().url(dns + getSettings().getInfoContextPath());
                __return.add(server);
            }
        }
        return Collections.unmodifiableList(__return);
    }

}
