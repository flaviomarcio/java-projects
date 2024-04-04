package com.app.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
public class DateUtilTest {
    public static final LocalDate TEST_MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    public static final LocalTime TEST_MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime TEST_MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime TEST_MIN_LOCALDATETIME = LocalDateTime.of(TEST_MIN_LOCALDATE, TEST_MIN_LOCALTIME);
    public static final LocalDateTime TEST_MAX_LOCALDATETIME = LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME);


    @Test
    public void UI_CHECK_CONSTS()
    {
        Assertions.assertEquals(DateUtil.MIN_LOCALDATE,TEST_MIN_LOCALDATE);
        Assertions.assertEquals(DateUtil.MAX_LOCALTIME,TEST_MAX_LOCALTIME);
        Assertions.assertEquals(DateUtil.MIN_LOCALTIME,TEST_MIN_LOCALTIME);
        Assertions.assertEquals(DateUtil.MIN_LOCALDATETIME,TEST_MIN_LOCALDATETIME);

    }

    @Test
    public void UI_toMax_LocalTime() {
        var value = LocalTime.now();
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax((LocalTime)null).toString(), TEST_MAX_LOCALDATETIME.toString());
    }

    @Test
    public void UI_toMax_LocalDate() {

        var value = LocalDate.now();
        Assertions.assertNotNull(DateUtil.toMax((LocalDate)null));
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax((LocalDate)null).toString(), TEST_MAX_LOCALDATETIME.toString());
    }

    @Test
    public void UI_toMax_LocalDateTime() {

        var value = LocalDateTime.now();
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax((LocalDateTime)null), TEST_MAX_LOCALDATETIME);
    }

    @Test
    public void UI_toMin_LocalTime() {
        var value = LocalTime.now();
        Assertions.assertEquals(DateUtil.toMin(value).toString(), TEST_MIN_LOCALDATETIME.toString());
        Assertions.assertEquals(DateUtil.toMin((LocalTime)null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

    @Test
    public void UI_toMin_LocalDate() {

        var value = LocalDate.now();
        Assertions.assertEquals(DateUtil.toMin(value).toString(), LocalDateTime.of(value, TEST_MIN_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMin((LocalDate)null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

    @Test
    public void UI_toMin_LocalDateTime() {

        var value = LocalDateTime.now();
        Assertions.assertEquals(DateUtil.toMin(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MIN_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMin((LocalDateTime)null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

}
