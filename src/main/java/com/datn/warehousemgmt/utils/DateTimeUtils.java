package com.datn.warehousemgmt.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatLocalDateTime(LocalDateTime dateTime){
        return dateTime.format(dateTimeFormatter);
    }
}
