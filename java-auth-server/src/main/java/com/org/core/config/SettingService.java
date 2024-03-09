package com.org.core.config;

import com.littlecode.util.SystemUtil;
import com.org.business.domain.AuthMode;
import com.org.business.domain.SetupMode;
import com.org.business.dto.TokenSetting;
import com.org.business.exceptions.AuthGenericException;
import com.org.core.helper.MsgHelper;
import com.org.core.utils.HashUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Getter
@Slf4j
@Service
@Configuration
@RequiredArgsConstructor
public class SettingService {

    private static boolean LOADED = false;

    private final Environment environment;
    private String auth_server_config_path_by_scope;
    private String auth_server_config_path_by_token;
    private boolean auth_server_debug;
    private SetupMode auth_server_scope_setup_mode;
    private UUID auth_server_scope_id;
    private String auth_server_scope_name;
    private String auth_server_token_secret;
    private String auth_server_token_application_name;
    private int auth_server_token_expires_days;
    private int auth_server_token_expires_minutes;
    private AuthMode auth_server_auth_mode;

    private static void mkPath(String path) {
        File configPath = new File(path);
        if (!configPath.exists()) {
            if (!configPath.mkdir())
                throw AuthGenericException.e(MsgHelper.PATH_ERROR_ON_CREATE, path);
        }

    }

    public static String getSchemaName() {
        return SystemUtil.Env.getProperty("spring.datasource.schema");
    }

    private static void cpPath(String src, String dst) {
        if (src == null || dst == null)
            return;
        log.debug("Copying files from [{}] to [{}] ", src, dst);
        var srcPath = new File(src);
        var dstPath = new File(dst);
        if (!srcPath.exists())
            log.debug(" - Copying skipped, source path not found: [{}]", srcPath);
        else if (!dstPath.exists())
            log.debug(" - Copying skipped, destine path not found: [{}]", dstPath);
        else {
            var srcFiles = srcPath.listFiles();
            if (srcFiles == null) {
                log.debug(" - Copying skipped, no files to copy");
                return;
            }
            {
                log.debug("  Copying path src: [{}] destine: [{}]", srcPath, dstPath);
                for (var srcFile : srcFiles) {
                    if (!srcFile.isFile()) {
                        log.debug("      -Copying skipped, is not a file: [{}]", srcFile);
                    }
                    var originPath = srcFile.toPath();
                    var destinePath = new File(dstPath, srcFile.getName()).toPath();
                    log.debug("      -Copying src: [{}] destine: [{}]", originPath, destinePath);
                    try {
                        Files.copy(originPath, destinePath, StandardCopyOption.REPLACE_EXISTING);
                        log.error("     -Copying success: src: [{}] destine: [{}]", originPath, destinePath);
                    } catch (IOException e) {
                        throw AuthGenericException.e("     -Copying fail: src: [%s] destine: [%s]", originPath, destinePath);
                    }
                }
            }
        }
        log.debug("Copying files finished");
    }

    public void init() {

        auth_server_scope_name = environment.getProperty("service.scope.name");
        auth_server_scope_id = HashUtil.toMd5Uuid(auth_server_scope_name);
        if (auth_server_scope_id == null)
            throw AuthGenericException.e("Invalid scope name, check env: [auth-server.scope.name]");

        auth_server_debug = Boolean.parseBoolean(environment.getProperty("service.debug"));
        auth_server_scope_setup_mode = SetupMode.of(environment.getProperty("service.scope.setup.mode", SetupMode.DISABLED.value()));
        var auth_server_config_path = environment.getProperty("service.config.path");
        var auth_server_config_template_cert = environment.getProperty("service.config.template.cert");
        auth_server_token_application_name = environment.getProperty("service.name", "");
        auth_server_token_secret = environment.getProperty("service.token.secret", "");
        auth_server_token_expires_days = Integer.parseInt(Objects.requireNonNull(environment.getProperty("service.token.expires.days", "7")));
        auth_server_token_expires_minutes = Integer.parseInt(Objects.requireNonNull(environment.getProperty("service.token.expires.minutes", "7")));
        auth_server_auth_mode = AuthMode.of(environment.getProperty("service.auth.mode", AuthMode.USERNAME.name()));
        var auth_server_admin_username = String.valueOf(environment.getProperty("service.scope.admin.username")).trim().toLowerCase();
        var auth_server_context_path = environment.getProperty("service.context.path", "/api").trim().toLowerCase();

        auth_server_config_path_by_scope = (auth_server_config_path == null || auth_server_config_path.trim().isEmpty())
                ? System.getProperty("user.home")
                : auth_server_config_path;

        if (auth_server_config_path_by_scope == null)
            throw AuthGenericException.e("Invalid config path");
        mkPath(auth_server_config_path_by_scope);

        auth_server_config_path_by_scope = (auth_server_scope_name == null || auth_server_scope_name.trim().isEmpty())
                ? auth_server_config_path_by_scope + "/auth-server"
                : auth_server_config_path_by_scope + "/" + auth_server_scope_name;
        mkPath(auth_server_config_path_by_scope);

        auth_server_config_path_by_scope = auth_server_config_path_by_scope + "/config";
        mkPath(auth_server_config_path_by_scope);

        auth_server_config_path_by_token = auth_server_config_path_by_scope + "/token";
        mkPath(auth_server_config_path_by_token);

        cpPath(auth_server_config_template_cert, auth_server_config_path_by_token);

        log.debug("Initialization completed");

        if (LOADED)
            return;

        log.info("..auth-server:");
        log.info("....debug: {}", auth_server_debug);
        log.info("....scope:");
        log.info("......setup:");
        log.info("........mode: {}", auth_server_scope_setup_mode);
        log.info("......id: {}", auth_server_scope_id);
        log.info("......name: {}", auth_server_scope_name);
        log.info("......admin:");
        log.info("........username: {}", auth_server_admin_username);
        log.info("....auth:");
        log.info("......mode: {}", auth_server_auth_mode);
        log.info("....config:");
        log.info("......path: {}", auth_server_config_path);
        log.info("......scope: {}", auth_server_config_path_by_scope);
        log.info("......token: {}", auth_server_config_path_by_token);
        log.info("......template");
        log.info("........cert: {}", auth_server_config_template_cert);
        log.info("....context:");
        log.info("......path: {}", auth_server_context_path);
        log.info("....token:");
        log.info("......expires:");
        log.info("........days: {}", auth_server_token_expires_days);
        log.info("........minutes: {}", auth_server_token_expires_minutes);

        LOADED = true;
    }

    public UUID getScopeId() {
        return auth_server_scope_id;
    }

    public String getScopeName() {
        return auth_server_scope_name;
    }

    public SetupMode getScopeSetupMode() {
        return auth_server_scope_setup_mode;
    }

    public boolean isDebugMode() {
        return auth_server_debug;
    }

    public String getTokensPath() {
        return auth_server_config_path_by_token;
    }

    public TokenSetting getTokenSetting() {
        String path = getTokensPath();
        return TokenSetting
                .builder()
                .path(path)
                .secret(auth_server_token_secret)
                .expires(
                        TokenSetting.Expires
                                .builder()
                                .days(auth_server_token_expires_days)
                                .minutes(auth_server_token_expires_minutes)
                                .build()
                )
                .access(
                        TokenSetting.Setting
                                .builder()
                                .keyPathPrivate(String.format("%s/access-token-private.key", path))
                                .keyPathPublic(String.format("%s/access-token-public.key", path))
                                .build())
                .refresh(
                        TokenSetting.Setting
                                .builder()
                                .keyPathPrivate(String.format("%s/refresh-token-private.key", path))
                                .keyPathPublic(String.format("%s/refresh-token-public.key", path))
                                .build())
                .build();
    }

}
