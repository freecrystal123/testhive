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
    private static DateTimeFormatter formatHours = DateTimeFormatter.ofPattern("dd/HH");

    public static void main(String[] args) {
       String[] aa =  timeutils.getPastAndFutureHours();
        for(String ss:aa){
            System.out.println(ss);
        }
    }
    public static  String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

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

    public static String getPast24Hours() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取当前时间减去24小时
        LocalDateTime past24Hours = now.minusHours(24);

        // 格式化并返回过去24小时的时间
        return past24Hours.format(formatter);
    }

    public static String getDayStart(){
        // 获取今天的 00:00:00
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        // 格式化输出
        System.out.println("今天开始时间：" + todayStart.format(formatter));
        return todayStart.format(formatter);
    }




    public static String[] getPastAndFutureHours() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 创建一个数组来存储过去24小时和未来7小时的日期
        String[] hoursArray = new String[31]; // 24 + 7 = 31

        // 填充过去24小时的时间
        for (int i = 0; i < 24; i++) {
            // 获取当前时间减去i小时的时间
            LocalDateTime pastHour = now.minusHours(i); // 需要加1，因为i从0开始
            hoursArray[i] = pastHour.format(formatHours); // 格式化为"MM/dd"
        }

        // 填充接下来的7小时
        for (int i = 0; i < 7; i++) {
            // 获取当前时间加上i小时的时间
            LocalDateTime futureHour = now.plusHours(i + 1); // 需要加1，因为i从0开始
            hoursArray[24 + i] = futureHour.format(formatHours); // 格式化为"MM/dd"
        }

        return hoursArray;
    }


    public static String getTwoDaysAgoStart() {
        // 获取2天前的开始时间 (00:00:00)
        LocalDateTime twoDaysAgoStart = LocalDate.now().minusDays(2).atStartOfDay();
        // 格式化输出
        System.out.println("两天前开始时间：" + twoDaysAgoStart.format(formatter));
        return twoDaysAgoStart.format(formatter);
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
