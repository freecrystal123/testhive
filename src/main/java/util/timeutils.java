package util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;
public class timeutils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter formatHours = DateTimeFormatter.ofPattern("dd/MM:HH");

    public static void main(String[] args) {
        System.out.println(getTimezone("5.195.113.230"));

    }
    public static  String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    public static long convertToTimeStamp(String inputdate){
        long timestamp = 0;
        try {
            // 将字符串解析为日期
            // 将字符串解析为 LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(inputdate, formatter);

            // 获取时间戳（秒）
             timestamp = dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();


            // 输出时间戳
            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }
       return timestamp;
    }

    public static String getTimezone(String ip) {
        try {
            // 创建一个 URL 对象，指向 ipinfo.io 的接口
            String urlString = "http://ipinfo.io/" + ip + "/json";  // 使用特定的 IP 地址
            System.out.println("urlString---"+urlString);
            URL url = new URL(urlString);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");  // 使用 GET 请求

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // 将响应内容读取到 response 字符串
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 解析 JSON 数据
            JSONObject jsonResponse = new JSONObject(response.toString());
            // 获取 timezone 字段
            return jsonResponse.getString("timezone");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";  // 如果出错，返回 Error 字符串
        }
    }


    public static double calculateAverage(List<Integer> arr) {
        // 求和
        double sum = 0;
        for (int num : arr) {
            sum += num;
        }

        // 计算平均值
        return sum / arr.size();
    }


    public static double findMedian(List<Integer> arr){
        // 排序数组
        Collections.sort(arr);

        // 获取数组长度
        int length = arr.size();

        // 如果数组长度为奇数，中位数是中间的元素
        if (length % 2 == 1) {
            return arr.get(length / 2);
        } else {
            // 如果数组长度为偶数，中位数是中间两个元素的平均值
            int mid1 = arr.get(length / 2 - 1);
            int mid2 = arr.get(length / 2);
            return (mid1 + mid2) / 2.0;
        }
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
        String[] hoursArray = new String[174]; // 24 + 7 = 31

        // 填充过去24小时的时间
        for (int i = 0; i < 168; i++) {
            // 获取当前时间减去i小时的时间
            LocalDateTime pastHour = now.minusHours(i); // 需要加1，因为i从0开始
            hoursArray[i] = pastHour.format(formatHours); // 格式化为"MM/dd"
            String firstpart = hoursArray[i].substring(0,6);
            StringBuffer hour = new StringBuffer();
            hour.append(hoursArray[i].substring(6,8));
            hour.append("-");
            int hourInt = (Integer.parseInt(hoursArray[i].substring(6,8))+1);
            String hourStr = String.valueOf(hourInt);
            if(hourInt<10){
                hourStr = "0"+hourInt;
            }
            hour.append(hourStr);
//            System.out.println(hour);
            hoursArray[i] = firstpart+hour.toString();

        }

        // 填充接下来的7小时
        for (int i = 0; i < 7; i++) {
            // 获取当前时间加上i小时的时间
            LocalDateTime futureHour = now.plusHours(i + 1); // 需要加1，因为i从0开始
            hoursArray[24 + i] = futureHour.format(formatHours); // 格式化为"MM/dd"
            StringBuffer hour = new StringBuffer();
            String firstpart = hoursArray[i].substring(0,6);
            hour.append(hoursArray[24 + i].substring(6,8));
            hour.append("-");
            int hourInt = (Integer.parseInt(hoursArray[24 + i].substring(6,8))+1);
            String hourStr = String.valueOf(hourInt);
            if(hourInt<10){
                hourStr = "0"+hourInt;
            }
            hour.append(hourStr);
//            System.out.println(hour);
            hoursArray[24 + i] = firstpart + hour.toString();
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
