package com.security.vinclub.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static LocalDateTime addSevenHours(LocalDateTime dateTime) {
        return dateTime.plusHours(7);
    }
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return dateTime.format(formatter);
    }

    public static LocalDateTime convertToGMTPlus7(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime gmtPlus7Zoned = localDateTime.atZone(ZoneId.of("GMT+7"));
        return gmtPlus7Zoned.toLocalDateTime();
    }



}
