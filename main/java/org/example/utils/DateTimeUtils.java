package org.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    public static String TIME_PARTTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String MONTH_PARTTERN = "yyyy-MM";
    public static String getLastDateCM(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Tính ngày cuối tháng
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return new SimpleDateFormat(TIME_PARTTERN).format(calendar.getTime());
    }

    public static String getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat(MONTH_PARTTERN).format(calendar.getTime());
    }
}
