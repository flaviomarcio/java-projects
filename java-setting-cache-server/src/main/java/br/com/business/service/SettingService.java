package br.com.business.service;

import br.com.business.dto.Setting;
import br.com.business.dto.SettingResponse;
import br.com.business.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
public class SettingService {
    private final Environment environment;
    private String service_config_path;
    public SettingService(Environment environment){
        this.environment=environment;
    }

    private static void mkPath(String path) {
        File configPath = new File(path);
        if (!configPath.exists()) {
            if (!configPath.mkdirs())
                throw new RuntimeException(String.format("Error on create path %s", path));
        }
    }

    private String makeFileName(String settingName) {
        return String.format("%s/%s.json", service_config_path, settingName);
    }

    public void init()
    {
        String service_context_port = environment.getProperty("service.context.port", "8080");
        String service_context_path = environment.getProperty("service.context.path", "/api").trim();
        String service_config_environment = environment.getProperty("service.config.environment", "development").trim();
        service_config_path=environment.getProperty("service.config.path","/tmp/setting-cache").trim();

        var dir = (service_config_path.isEmpty())
                ? String.format("%s/setting-cache", System.getProperty("user.home"))
                : service_config_path;

        service_config_path = String.format("%s/%s", dir, service_config_environment);
        mkPath(service_config_path);
        log.info("Listen: {}, configPath={}, context.path: {}", service_context_port,service_config_path, service_context_path);
    }

    public boolean exists(String settingName) {
        try {
            return new File(makeFileName(settingName)).exists();
        } catch (Exception ignored) {}
        return false;
    }

    public SettingResponse write(Setting setting) {
        if (setting == null || setting.getName() == null || setting.getName().isEmpty())
            return null;

        try (FileOutputStream fos = new FileOutputStream(makeFileName(setting.getName()))) {
            var data = ObjectUtil.toJson(setting);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return SettingResponse
                .builder()
                .id(UUID.randomUUID())
                .md5(UUID.randomUUID())
                .build();
    }

    public Setting read(String settingName) {
        File settingFile = new File(makeFileName(settingName));
        if (!settingFile.exists())
            return null;
        try {
            var jsonBytes = Files.readString(settingFile.toPath());
            var jsonNode = ObjectUtil.toJsonObject(jsonBytes);
            if (jsonNode == null)
                return null;
            if (!jsonNode.isObject())
                return null;

            return Setting
                    .builder()
                    .name(jsonNode.get("name").asText())
                    .settings(jsonNode.get("settings"))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Object setting(String settingName) {
        var setting = this.read(settingName);
        return setting == null || setting.getSettings() == null
                ? new HashMap<>()
                : setting.getSettings();
    }
}
