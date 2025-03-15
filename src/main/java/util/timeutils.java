package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class timeutils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String getNowTime(){

        long timestamp = System.currentTimeMillis(); // 获取当前时间戳（毫秒）
        // 转换为 LocalDateTime
        LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault()) // 使用系统默认时区
                .toLocalDateTime();
        // 格式化并输出
        String formattedTime = dateTime.format(formatter);
        System.out.println("转换后的时间：" + formattedTime);
        return formattedTime;
    }
    public static String getDayStart(){
        // 获取今天的 00:00:00
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        // 格式化输出
        System.out.println("今天开始时间：" + todayStart.format(formatter));
        return todayStart.format(formatter);
    }
    public static Date getConvertDate(String date){
        java.sql.Date sqlDate = null;
        try {
            // 1. 定义日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 2. 解析字符串为 java.util.Date
            java.util.Date utilDate = sdf.parse(date);

            // 3. 转换为 java.sql.Date
            sqlDate = new java.sql.Date(utilDate.getTime());

            System.out.println("java.sql.Date: " + sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }
    public static String get7DayAgo(){
        // 获取 7 天前的 00:00:00
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(7).atStartOfDay();

        // 定义格式化格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化输出
        System.out.println("7 天前开始时间：" + sevenDaysAgo.format(formatter));

        // 返回格式化后的时间字符串
        return sevenDaysAgo.format(formatter);
    }
}
