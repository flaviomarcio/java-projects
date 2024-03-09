package br.com.tests;

import br.com.business.dto.Setting;
import br.com.business.service.SettingService;
import br.com.business.util.ObjectUtil;
import br.com.config.TestConfig;
import br.com.factory.FactoryByTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = {TestConfig.class})
@Slf4j
public class ServiceTest {
    private final FactoryByTests factoryByTests = new FactoryByTests();
    private final List<Setting> recordList = factoryByTests.getSettingList();
    private final SettingService service = factoryByTests.getSettingService();

    @Test
    public void UT_CHECK_read_writer() {
        for (var recordSrc : recordList) {
            Assertions.assertNotNull(service.write(recordSrc));
            Assertions.assertNotNull(service.read(recordSrc.getName()));
            Assertions.assertNotNull(service.setting(recordSrc.getName()));
            Assertions.assertTrue(service.exists(recordSrc.getName()));
        }

        for (var recordSrc : recordList) {
            if (recordSrc.getName().equals("d3d94468-02a4-3259-b55d-38e6d163e820"))
                log.info(recordSrc.getName());
            var recordGet = service.read(recordSrc.getName());

            Assertions.assertNotNull(recordGet);
            var md5Src = ObjectUtil.toMd5(recordSrc.getSettings());
            var md5Get = ObjectUtil.toMd5(recordGet.getSettings());

            Assertions.assertEquals(md5Src, md5Get);
        }

        for (var recordSrc : recordList) {
            var recordGet = service.setting(recordSrc.getName());
            Assertions.assertNotNull(recordGet);
            var md5Src = ObjectUtil.toMd5(recordSrc.getSettings());
            var md5Get = ObjectUtil.toMd5(recordGet);
            Assertions.assertEquals(md5Src, md5Get);
        }
    }

    @Test
    public void UT_CHECK_read_write_fail() {
        var list = List.of(
                Setting.builder().build(),
                Setting.builder().settings(List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())).build()
        );
        for (var record : list) {
            Assertions.assertNull(service.write(record));
        }

        for (var settingName : List.of("test-1", UUID.randomUUID().toString(), UUID.randomUUID().toString())) {
            Assertions.assertNull(service.read(settingName));
            Assertions.assertNotNull(service.setting(settingName));
            Assertions.assertFalse(service.exists(settingName));
        }

    }

}