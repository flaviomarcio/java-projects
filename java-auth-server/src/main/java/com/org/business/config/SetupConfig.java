package com.org.business.config;

import com.littlecode.setup.Setup;
import com.org.business.domain.SetupMode;
import com.org.business.exceptions.AuthGenericException;
import com.org.business.model.DbTable;
import com.org.business.model.Scope;
import com.org.business.model.User;
import com.org.commons.utils.Singletons;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SetupConfig {
    private static final String SPRING_DATASOURCE_SCHEMA_NAME = "${spring.datasource.schema}";
    private final Singletons singletons;

    @Value("${spring.datasource.schema:}")
    private String spring_datasource_schema_name;

    @Bean(name = Setup.STP_CONFIGURE_BEAN_NOTIFY)
    public Setup.ExecutorNotify configureNotify() {
        return
                Setup.ExecutorNotify
                        .builder()
                        .started(() -> log.debug("setup: started"))
                        .prepare(target -> {
                            var ddl = target.getDatabase().getDDL();
                            ddl.setPackageNames(DbTable.class.getPackageName());
                        })
                        .successful(() -> {
                        })
                        .fail(() -> {
                        })
                        .finished(() -> log.debug("setup: completed"))
                        .build();
    }

    @Bean(name = Setup.STP_CONFIGURE_BEAN_DATABASE)
    public Setup.ExecutorDataBase configureDatabase() {
        log.debug("configureDatabase: executing");
        return Setup.ExecutorDataBase
                .builder()
                .before(() -> log.debug("db: executing"))
                .checker(() -> !singletons.getScopeService().isMounted())
                .sourceExecute(statementItem -> log.debug(statementItem.toString()))
                .after(() -> log.debug("db: finished"))
                .build();
    }

    @Bean(name = Setup.STP_CONFIGURE_BEAN_BUSINESS)
    public Setup.ExecutorBusiness configureBusiness() {
        log.debug("configureDatabase: executing");
        return
                Setup.ExecutorBusiness
                        .builder()
                        .before(() -> log.debug("business: executing"))
                        .executor(this::configureBusinessExec)
                        .after(() -> log.debug("business: finished"))
                        .build();

    }

    @Transactional
    public boolean configureBusinessExec() {
        var auth_server_scope_setup = singletons.getSettingService().getScopeSetupMode();
        if (!auth_server_scope_setup.equals(SetupMode.AUTO))
            return true;

        var scopeService = singletons.getScopeService();
        var userService = singletons.getUserService();
        var environment = singletons.getSettingService().getEnvironment();

        var scopeSystem = scopeService.getScopeSystem();
        if (scopeSystem == null) {
            var auth_server_scope_id = singletons.getSettingService().getScopeId();
            var auth_server_scope_name = String.valueOf(singletons.getSettingService().getScopeName());

            if (auth_server_scope_id == null)
                throw new RuntimeException("invalid scope name");

            scopeSystem = Scope
                    .builder()
                    .dt(LocalDateTime.now())
                    .id(auth_server_scope_id)
                    .name(auth_server_scope_name)
                    .enabled(true)
                    .build();
            scopeService.save(scopeSystem);
            log.info("     Scope created: scopeId: {}, scopeName: {}", scopeSystem.getId(), scopeSystem.getId());
        }

        if (!userService.existsUsers()) {
            var auth_server_admin_username = String.valueOf(environment.getProperty("service.scope.admin.username")).trim().toLowerCase();
            var auth_server_admin_password = String.valueOf(environment.getProperty("service.scope.admin.password")).trim();

            if (!auth_server_admin_username.trim().isEmpty() && auth_server_admin_password.trim().isEmpty())
                throw AuthGenericException.e("    Invalid scope user admin password");

            if (!auth_server_admin_username.isEmpty()) {
                var user = User.builder()
                        .scopeId(scopeSystem.getId())
                        .username(auth_server_admin_username)
                        .password(auth_server_admin_password)
                        .document("99988877766")
                        .dtBirth(LocalDate.of(1901, 1, 1))
                        .email(String.format("%s@admin.com", auth_server_admin_username))
                        .phoneNumber("5599877665544")
                        .enabled(true)
                        .deleted(false)
                        .build();
                log.info("     User admin created: scopeId: {}, userName: {}, password: {}", user.getScopeId(), user.getUsername(), user.getPassword());
                singletons.getUserService().createUser(user);
            }
        }
        return true;
    }
}
