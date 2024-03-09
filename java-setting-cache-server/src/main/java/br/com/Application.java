package br.com;

import br.com.business.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    @Autowired
    private SettingService settingService;

    @PostConstruct
    private void postConstruct(){
        settingService.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
