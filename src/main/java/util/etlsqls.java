package util;

import com.google.gson.*;
import jdbc.dmlacid;
import jdbc.mysqljdbcconn;
import jdbc.sqlserverjdbcconn;
import pojp.*;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class etlsqls {
    // 创建 GsonBuilder 并注册自定义的 JsonDeserializer

    // 创建自定义日期格式
    static String  os = System.getProperty("os.name").toLowerCase();


    // 定义JDBC 数据库连接
    static Properties financeJDBC = null;
    static {
        financeJDBC = new Properties();
        financeJDBC.put("jdbcurl", "jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        financeJDBC.put("username","Viviene");
        financeJDBC.put("password","VALe@1234");


        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            context.getClientSessionContext().setSessionTimeout(0);
            context.getClientSessionContext().setSessionCacheSize(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 创建自定义日期格式
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 创建自定义的 JsonDeserializer
    static JsonDeserializer<Date> dateDeserializer = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return sdf.parse(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException("Unparseable date: " + timeutils.getCurrentTime() + json.getAsString(), e);
            }
        }
    };

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, dateDeserializer)
            .create();


    // path
    public static String macbasepath = "/Users/Shared/";
    public static String winbasepath = "C:/Windows/Temp/";
    public static String basepath = null;
    static boolean macflag = true;
    static {
        if (os.contains("win")) {
            macflag = false;
        }
        if(macflag){
            basepath = macbasepath;
        } else {
            basepath = winbasepath;
        }
    }

    // 默认输出路径
    public static String orderwintosqlserver2FilePath = basepath+"orderwintosqlserver2FilePath.csv";

    public static String trafficdatatempFilePath = basepath+"trafficdatatempFilePath.csv";

    public static String ftdFilePath = basepath+"ftdFilePath.csv";

    public static String userinfo2FilePath  = basepath+"userinfo2FilePath.csv";

    public static String failreason2FilePath  = basepath+"failreason2FilePath.csv";

    public static String currentfailcount2FilePath = basepath+"currentfailcount2FilePath.csv";

    public static String failreasondetails2FilePath  = basepath+"failreasondetails2FilePath.csv";

    public static String failreasonhis2FilePath  = basepath+"failreasonhis2FilePath.csv";

    public static String rgusersstatics2FilePath  = basepath+"rgusersstatics2FilePath.csv";


    public static String rgdispositedlimitselftimeout2FilePath =  basepath +"rgdispositedlimitselftimeout2FilePath.csv";



    public static StringBuffer msg = new StringBuffer();
    public static StringBuffer logger = new StringBuffer();



    public static void main(String[] args) throws Exception{
//        last7days_rate();
//        fail_reason_monitoring();
//        fail_reason_monitordetail();
//        fail_reason_monitordetail2();
        orderwintosqlserver();
        //rgusersstatics();
        //rgdispositedlimitselftimeout();
//        fail_current_fail_count();

    }
    public static String logs1 ;
    public static void InLog(String logs){
        logger.append(logs+"\n");
        logs1 = logger.toString();
    }
    public static String getLog(){
        return logs1;
    }

    public static List<factjobscheduler> listScheduerInfos(){
        List<factjobscheduler> factjobschedulers = null;
        try {
          factjobschedulers =  dmlacid.listTableRecord(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),"fact_job_scheduler",  factjobscheduler.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factjobschedulers;
    }
    /*  */
    public static int rgdispositedlimitselftimeout(  ) throws Exception {

        // 获取昨天的日期
        LocalDate yesterday = LocalDate.now();

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将昨天的日期格式化为字符串
        String formattedDate = yesterday.format(formatter);
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select abcd.exclusion_date dateid,self_exclusion_users,timeout_users,deposit_limit_users,deposit_limit_atreg_users\n" +
                "from \n" +
                "(\n" +
                "select \n" +
                "exclusion_date\n" +
                ",sum(case when exclusion_type = 'Deposit Limit' then 1 else 0 end ) deposit_limit_users\n" +
                ",sum(case when exclusion_type = 'Self-Exclusion' then 1 else 0 end ) self_exclusion_users\n" +
                ",sum(case when exclusion_type = 'Time-Out' then 1 else 0 end ) timeout_users\n" +
                "from users join  (\n" +
                "select distinct user_id ,exclusion_type, substr(cast( EPOCH_TO_TIMESTAMP(exclusion_start_time) as string),1,10)  exclusion_date\n" +
                "from events where event = 'user_exclusion'\n" +
                "and exclusion_type in( 'Deposit Limit' ,'Self-Exclusion','Time-Out')) aa\n" +
                "on users.id = aa.user_id\n" +
                "where exclusion_date  < '" + formattedDate + "'\n" +
                "group by exclusion_date ) abcd\n" +
                "left join (\n" +
                "select count(distinct uid ) deposit_limit_atreg_users ,exclusion_date from users join (\n" +
                "select distinct aa.user_id,exclusion_date from ( \n" +
                "select substr(cast(time as string) ,1,10) registed_date ,user_id\n" +
                "from events where event='register_new'  ) aa\n" +
                "join (\n" +
                "select distinct user_id , substr(cast( EPOCH_TO_TIMESTAMP(exclusion_start_time) as string),1,10)  exclusion_date\n" +
                "from events where event = 'user_exclusion'\n" +
                "and exclusion_type in( 'Deposit Limit') ) bb \n" +
                "on aa.user_id = bb.user_id\n" +
                "and aa.registed_date = bb.exclusion_date ) abc on users.id = abc.user_id \n" +
                "group by exclusion_date ) abcc on  abcd.exclusion_date = abcc.exclusion_date \n" ,
                "--data-urlencode", "format=json",
        };



        // 输出 逻辑

        BufferedWriter writer = new BufferedWriter(new FileWriter(rgdispositedlimitselftimeout2FilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;
        while ((line = reader.readLine()) != null) {
            userrglimitinfo person = gson.fromJson(line, userrglimitinfo.class);
            writer.write(person.dateid+","+person.self_exclusion_users+","+person.timeout_users+","+person.deposit_limit_users+","+person.deposit_limit_atreg_users);
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成

        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();
        /** 阿里云插入限制 **/
        // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
        // 通过 excel load fail 导入
        InLog("rgusersstatics2FilePath:"+rgdispositedlimitselftimeout2FilePath);

        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),rgdispositedlimitselftimeout2FilePath,"fact_lottery_userrglimitinfo_d",userrglimitinfo.class,null,null));

        return 0;



    }

    public static int last7days_rate() throws Exception{


        String starttime = timeutils.getDayStart();
        String before7days =  timeutils.get7DayAgo();
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select substr(cast(time as string),6,8) last7day\n" +
                ",round(sum(case when is_success=1 then 1 else 0 end )/sum(1) ,2) last7days_rate\n" +
                "from  events \n" +
                "where event = 'recharge_result'\n" +
                "and time > '"+before7days+"'\n" +
                "and time < '"+starttime+"'\n" +
                "group by substr(cast(time as string),6,8) \n" +
                "order by  substr(cast(time as string),6,8)   ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑


        BufferedWriter writer = new BufferedWriter(new FileWriter(failreasonhis2FilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;
        while ((line = reader.readLine()) != null) {
            failmonitoringhistorycal person = gson.fromJson(line, failmonitoringhistorycal.class);
            writer.write(person.last7day+","+person.last7days_rate);
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成

        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();
        /** 阿里云插入限制 **/
        // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
        // 通过 excel load fail 导入
        InLog("failreason2FilePath:"+failreason2FilePath);
        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),failreasonhis2FilePath,"fact_fail_rate_his_d",failmonitoringhistorycal.class,null,null));
        return 0;

    }


    public static int fail_reason_monitordetail2()  throws Exception {

        String starttime = timeutils.getTwoDaysAgoStart();
        String endtime = timeutils.getNowTime();
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= SELECT \n" +
                "    aaa.hour,\n" +
                "    bbb.country,\n" +
                "    case when (bbb.city = 'null' or bbb.city is null)  then 'Other' else bbb.city end  city,\n" +
                "    bbb.fail_reason,\n" +
                "    aaa.fail_total_num,\n" +
                "    bbb.fail_num\n" +
                "FROM (\n" +
                "    -- Subquery aaa: Counts total number of failed recharge attempts per hour\n" +
                "    SELECT \n" +
                "        CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)) AS hour,\n" +
                "        COUNT(1) AS fail_total_num\n" +
                "    FROM events\n" +
                "    WHERE event = 'recharge_result' \n" +
                "        AND recharge_method = 'PayBy Direct Payment'\n" +
                "        AND time > '"+starttime+"'\n" +
                "        AND time < '"+endtime+"'\n" +
                "        AND fail_reason IS NOT NULL \n" +
                "        AND fail_reason != 'TIMEOUT'\n" +
                "    GROUP BY CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))\n" +
                ") aaa\n" +
                "LEFT JOIN (\n" +
                "    -- Subquery bbb: Counts failed recharge attempts per hour, country, city, and reason\n" +
                "    SELECT \n" +
                "        CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)) AS hour,\n" +
                "        users.country,\n" +
                "        users.city,\n" +
                "        fail_reason,\n" +
                "        COUNT(1) AS fail_num\n" +
                "    FROM events\n" +
                "    JOIN users ON events.user_id = users.id\n" +
                "    WHERE event = 'recharge_result' \n" +
                "        AND recharge_method = 'PayBy Direct Payment'\n" +
                "        AND time > '"+starttime+"'\n" +
                "        AND time < '"+endtime+"'\n" +
                "        AND fail_reason IS NOT NULL \n" +
                "        AND fail_reason != 'TIMEOUT'\n" +
                "    GROUP BY \n" +
                "        CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)),\n" +
                "        users.country,\n" +
                "        users.city,\n" +
                "        fail_reason\n" +
                ") bbb ON aaa.hour = bbb.hour " +
                "      ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑


        BufferedWriter writer = new BufferedWriter(new FileWriter(failreasondetails2FilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;
        while ((line = reader.readLine()) != null) {
            failmonitorindetail2 person = gson.fromJson(line, failmonitorindetail2.class);
            writer.write(person.hour + "," +person.city + ","  +person.province + "," +person.country + ","  +"\""+ person.fail_reason + "\"" + "," + person.fail_total_num  + "," + person.fail_num);
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成

        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();
        /** 阿里云插入限制 **/
        // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
        // 通过 excel load fail 导入
        InLog("failreasondetails2FilePath:"+failreasondetails2FilePath);
        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),failreasondetails2FilePath,"fact_fail_monitoring_details2_h",failmonitorindetail2.class,null,null));
        return 0;


    }



     public static int fail_reason_monitordetail()  throws Exception {

         String starttime = timeutils.getDayStart();
         String endtime = timeutils.getNowTime();
         String[] command = {
                 "curl",
                 "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                 "-X", "POST",
                 "--data-urlencode", "q= SELECT \n" +
                 "    SUBSTR(CAST(time AS STRING), 12, 2) AS hour,\n" +
                 "    $city AS city,\n" +
                 "    $province AS province,\n" +
                 "    $country AS country,\n" +
                 "    fail_reason,\n" +
                 "    COUNT(1) AS fail_count,\n" +
                 "    round(COUNT(1) * 1.0 / SUM(COUNT(1)) OVER (PARTITION BY SUBSTR(CAST(time AS STRING), 12, 2)) ,2) AS fail_ratio\n" +
                 "FROM events\n" +
                 "WHERE event = 'recharge_result' \n" +
                 "    AND recharge_method = 'PayBy Direct Payment'\n" +
                 "    and time > '"+starttime+"'\n" +
                 "    and time < '"+endtime+"'\n" +
                 "    AND fail_reason IS NOT NULL and fail_reason != 'TIMEOUT' \n" +
                 "GROUP BY \n" +
                 "    SUBSTR(CAST(time AS STRING), 12, 2),\n" +
                 "    recharge_method," +
                 "    $city,\n" +
                 "    $country,\n" +
                 "    $province,\n" +
                 "    fail_reason;\n" +
                 "      ",
                 "--data-urlencode", "format=json",
         };


         // 输出 逻辑


         BufferedWriter writer = new BufferedWriter(new FileWriter(failreasondetails2FilePath));

         // 执行 curl 命令
         Process process = Runtime.getRuntime().exec(command);
         // 读取命令输出
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         String line;
         int databaseoutputcount = 0;
         while ((line = reader.readLine()) != null) {
             failmonitorindetail person = gson.fromJson(line, failmonitorindetail.class);
             writer.write(person.hour + "," +person.city + ","  +person.province + "," +person.country + ","  +"\""+ person.fail_reason + "\"" + "," + person.fail_count  + "," + person.fail_ratio);
             writer.newLine();
             databaseoutputcount ++;
             if(databaseoutputcount%10==0){
                 System.out.println(" 目前是"+databaseoutputcount+"\n");
             }
         }
         InLog(msg.toString());
         // 等待命令执行完成

         if(databaseoutputcount==0){
             InLog("please check vpn !");
             throw new Exception("please check vpn!");
         }

         int exitCode = process.waitFor();
         if (exitCode == 0) {
             System.out.println("Curl command executed successfully ！");
             InLog( msg + "Curl command executed successfully ！");
         } else {
             System.out.println("Curl command failed with exit code: " + exitCode);
             InLog( msg + "Curl command failed with exit code: " + exitCode);

         }

         writer.close();
         /** 阿里云插入限制 **/
         // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
         // 通过 excel load fail 导入
         InLog("failreasondetails2FilePath:"+failreasondetails2FilePath);
         InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),failreasondetails2FilePath,"fact_fail_monitoring_details_h",failmonitorindetail.class,null,null));
         return 0;


     }



    public static int fail_current_fail_count() throws Exception{

        String starttime = timeutils.getDayStart();
        String endtime = timeutils.getNowTime();

        StringBuffer SQLBuffer = new StringBuffer();

        SQLBuffer.append("SELECT\n" +
                "    hour,\n" +
                "    current_fail_count\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)) AS hour,\n" +
                "        SUM(CASE WHEN is_success = 0 THEN 1 ELSE 0 END) AS current_fail_count,\n" +
                "        SUM(1) AS all_count,\n" +
                "        ROW_NUMBER() OVER (ORDER BY CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))) AS rn\n" +
                "    FROM events\n" +
                "    WHERE event = 'recharge_result'\n" +
                "    and recharge_method = 'PayBy Direct Payment' " +
                "    and fail_reason IS NOT NULL  AND fail_reason != 'TIMEOUT'" +
                "        AND time > '"+starttime+"'\n" +
                "        AND time < '"+endtime+"'\n" +
                "    GROUP BY CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))\n" +
                ") AS result\n" +
                "WHERE rn = (\n" +
                "    SELECT MAX(rn)\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            ROW_NUMBER() OVER (ORDER BY CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))) AS rn\n" +
                "        FROM events\n" +
                "        WHERE event = 'recharge_result'\n" +
                "    and recharge_method = 'PayBy Direct Payment' " +
                "    and fail_reason IS NOT NULL  AND fail_reason != 'TIMEOUT'" +
                "            AND time > '"+starttime+"'\n" +
                "            AND time < '"+endtime+"'\n" +
                "        GROUP BY CONCAT(SUBSTR(CAST(time AS STRING), 9, 2), '/', SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))\n" +
                "    ) AS subquery\n" +
                ");");



        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= "+SQLBuffer,
                "--data-urlencode", "format=json",
        };
        BufferedWriter writer = new BufferedWriter(new FileWriter(currentfailcount2FilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;
        while ((line = reader.readLine()) != null) {
            currentfailcount person = gson.fromJson(line, currentfailcount.class);
            writer.write(person.hour+","+person.current_fail_count);
            databaseoutputcount ++;
        }
        InLog(msg.toString());
        // 等待命令执行完成

        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();
        /** 阿里云插入限制 **/
        // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
        // 通过 excel load fail 导入
        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),currentfailcount2FilePath,"fact_current_fail_count_h",currentfailcount.class,null,null));
        return 0;


    }





     public static int fail_reason_monitoring() throws Exception{

        String starttime = timeutils.getDayStart();
        String past24Hours = timeutils.getPast24Hours();
        String endtime = timeutils.getNowTime();
        String before7days =  timeutils.get7DayAgo();

        StringBuffer SQLBuffer = new StringBuffer();
         SQLBuffer.append(
                         "select \n" +
                         "aahour.hour,\n" +
                         "avg7days_count,\n" +
                         "round(nvl(fail_count/all_count,0),2) fail_rate,\n" +
                         "nvl(fail_count,0) fail_count,\n" +
                         "nvl(all_count,0) all_count\n" +
                         "from (\n" );
         int totalcount = timeutils.getPastAndFutureHours().length;
         int processint = 0;
         for(String hours:timeutils.getPastAndFutureHours()){
             processint ++;
                 if(processint==totalcount){
                 SQLBuffer.append("SELECT ").append("'"+hours+"'").append(" as hour");
             } else{
                 SQLBuffer.append("SELECT ").append("'"+hours+"'").append(" as hour").append("\n union ");
             }

         }
         String sqlLast = ") aahour cross join (\n" +
                 "select round(avg(sum7days_rate),0) avg7days_count from (\n" +
                 "select \n" +
                 "round(sum(case when is_success=0  AND recharge_method = 'PayBy Direct Payment' and fail_reason IS NOT NULL  AND fail_reason != 'TIMEOUT' then 1 else 0 end ) ,2) sum7days_rate,\n" +
                 "CONCAT(SUBSTR(CAST(time AS STRING), 9, 2),'/',SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)) hour\n" +
                 "from  events " +
                 "where event = 'recharge_result'" +
                 "and time > '"+before7days+"'\n" +
                 "and time < '"+starttime+"'\n" +
                 "group by CONCAT(SUBSTR(CAST(time AS STRING), 9, 2),'/',SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))) abc" +
                 ") bb left join (select \n" +
                 "CONCAT(SUBSTR(CAST(time AS STRING), 9, 2),'/',SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2)) hour\n" +
                 ",sum(case when is_success=0 and fail_reason IS NOT NULL  AND fail_reason != 'TIMEOUT' then 1 else 0 end ) fail_count\n" +
                 ",sum(1) all_count\n" +
                 "from  events \n" +
                 "where event = 'recharge_result'\n" +
                 "and time > '"+past24Hours+"'\n" +
                 "and time < '"+endtime+"'\n" +
                 "group by CONCAT(SUBSTR(CAST(time AS STRING), 9, 2),'/',SUBSTR(CAST(time AS STRING), 6, 2), ':', SUBSTR(CAST(time AS STRING), 12, 2))) aa on aahour.hour=aa.hour  ";
         SQLBuffer.append(sqlLast);



         String[] command = {
                 "curl",
                 "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                 "-X", "POST",
                 "--data-urlencode", "q= "+SQLBuffer,
                 "--data-urlencode", "format=json",
         };


         // 输出 逻辑


         BufferedWriter writer = new BufferedWriter(new FileWriter(failreason2FilePath));

         // 执行 curl 命令
         Process process = Runtime.getRuntime().exec(command);
         // 读取命令输出
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         String line;
         int databaseoutputcount = 0;
         while ((line = reader.readLine()) != null) {
             failmonitoring2 person = gson.fromJson(line, failmonitoring2.class);
             writer.write(person.hour+","+person.avg7days_count+","+person.fail_rate+","+person.fail_count+","+person.all_count);
             writer.newLine();
             databaseoutputcount ++;
             if(databaseoutputcount%10==0){
                 System.out.println(" 目前是"+databaseoutputcount+"\n");
             }
         }
         InLog(msg.toString());
         // 等待命令执行完成

         if(databaseoutputcount==0){
             InLog("please check vpn !");
             throw new Exception("please check vpn!");
         }

         int exitCode = process.waitFor();
         if (exitCode == 0) {
             System.out.println("Curl command executed successfully ！");
             InLog( msg + "Curl command executed successfully ！");
         } else {
             System.out.println("Curl command failed with exit code: " + exitCode);
             InLog( msg + "Curl command failed with exit code: " + exitCode);

         }

         writer.close();
         /** 阿里云插入限制 **/
         // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
         // 通过 excel load fail 导入
         InLog("failreason2FilePath:"+failreason2FilePath);
         InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),failreason2FilePath,"fact_fail_monitoring2_rate_h",failmonitoring2.class,null,null));
         return 0;


     }

    /* 直接调用*/

    public static int rgusersstatics(  ) throws Exception{

        // 获取昨天的日期
        LocalDate yesterday = LocalDate.now().minusDays(0);


        // 获取昨天的日期
        LocalDate lastMonthday = LocalDate.now().minusDays(8);

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

        // 将昨天的日期格式化为字符串
        String yesterformattedDate = yesterday.format(formatter);
        String lastmonthformattedDate = lastMonthday.format(formatter);
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= \n" +
                "SELECT \n" +
                "    aa.bettingdate dateid,\n" +
                "    aa.loginusers logged_users,\n" +
                "    bb.bettingusers  betting_users \n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        SUBSTR(CAST(date AS STRING), 1, 10) AS bettingdate, \n" +
                "        COUNT(DISTINCT user_id) AS loginusers\n" +
                "    FROM events \n" +
                "    WHERE event = 'login_result'\n" +
                "        AND is_success = 1\n" +
                "        AND date >= '"+lastmonthformattedDate+"' AND date<='"+yesterformattedDate+"' \n" +
                "    GROUP BY date \n" +
                ") aa \n" +
                "JOIN (\n" +
                "    SELECT \n" +
                "        SUBSTR(CAST(date AS STRING), 1, 10) AS logindate,  \n" +
                "        COUNT(DISTINCT user_id) AS bettingusers\n" +
                "    FROM events \n" +
                "    WHERE event = 'lottery_order_result'\n" +
                "        AND is_success = 1 \n" +
                "        AND date >= '"+lastmonthformattedDate+"' and date<='"+yesterformattedDate+"' \n" +
                "    GROUP BY date \n" +
                ") bb \n" +
                "ON aa.bettingdate = bb.logindate  ",
                "--data-urlencode", "format=json",

        };


        // 输出 逻辑


        BufferedWriter writer = new BufferedWriter(new FileWriter(rgusersstatics2FilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;
        while ((line = reader.readLine()) != null) {
            userbussinessinfo person = gson.fromJson(line, userbussinessinfo.class);
            writer.write(person.dateid+","+person.logged_users+","+person.betting_users+",0,0");
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成

        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();
        /** 阿里云插入限制 **/
        // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
        // 通过 excel load fail 导入
        InLog("rgusersstatics2FilePath:"+rgusersstatics2FilePath);

        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),rgusersstatics2FilePath,"fact_user_bussinessinfo_d",userbussinessinfo.class,lastmonthformattedDate,yesterformattedDate));


        return 0;
    }




    public static int failreason( String starttime,String endtime ) throws Exception{


        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select substr(cast(time as string)  ,1,10) dateId,\n" +
                "case when fail_reason='手机号未被认证' then 'The phone number has not been verified.' \n" +
                "     when fail_reason='用户不存在' then 'The user does not exist' \n" +
                "     when fail_reason='用户名或密码错误' then 'Incorrect username or password' \n" +
                "     when fail_reason='用户禁用' then 'Prohibited User' \n" +
                "     else   'The email has not been verified' end login_fail_reason,\n" +
                "     count(user_id) number_of_user \n" +
                "from events \n" +
                "where event  = 'login_result'\n" +
                "and is_success = 0 \n" +
                "and substr(cast(time as string)  ,1,10) >= '"+starttime+"'\n" +
                "and substr(cast(time as string)  ,1,10) <= '"+endtime+"'\n" +
                "group by fail_reason,substr(cast(time as string)  ,1,10)\n" +
                "order by fail_reason,substr(cast(time as string)  ,1,10) ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑

            BufferedWriter writer = new BufferedWriter(new FileWriter(failreason2FilePath));

            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                failreason person = gson.fromJson(line, failreason.class);
                writer.write(person.dateid+","+person.login_fail_reason+","+person.number_of_user);
//                        writer.write(person.uid+","+person.register_time);
                writer.newLine();
                databaseoutputcount ++;
                if(databaseoutputcount%10==0){
                    System.out.println(" 目前是"+databaseoutputcount+"\n");
                }
            }
            InLog(msg.toString());
            // 等待命令执行完成

             if(databaseoutputcount==0){
              InLog("please check vpn !");
                  throw new Exception("please check vpn!");
              }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Curl command executed successfully ！");
                InLog( msg + "Curl command executed successfully ！");
            } else {
                System.out.println("Curl command failed with exit code: " + exitCode);
                InLog( msg + "Curl command failed with exit code: " + exitCode);

            }

            writer.close();
            /** 阿里云插入限制 **/
            // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
            // 通过 excel load fail 导入
            InLog("failreason2FilePath:"+failreason2FilePath);
        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),failreason2FilePath,"fact_login_fail_reason_d",failreason.class,starttime,endtime));

            return 0;
    }



    public static int traffic_data_temp( String starttime,String endtime ) throws Exception{

        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select \n" +
                "datadate DateID,nvl(first_visit_source_type,'0') Channel,\n" +
                "count(distinct user_id) UV,\n" +
                "count(1) PV \n" +
                " from users join ( \n" +
                "select substr(cast (time as string),1,10) datadate\n" +
                ",user_id\n" +
                "from events\n" +
                "where event = '$pageview'   and time >= '"+starttime+" 00:00:00' and time <='" + endtime +"' \n" +
                ") aa on users.id = aa.user_id \n" +
                "group by first_visit_source_type,datadate  ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑
        BufferedWriter writer = new BufferedWriter(new FileWriter(trafficdatatempFilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;

        while ((line = reader.readLine()) != null) {
            trafficdatatemp person = gson.fromJson(line, trafficdatatemp.class);
            writer.write(person.dateid+","+person.channel+","+person.uv+","+person.pv);
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成,如果记录为空，是VPN没有连接
        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();

        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),trafficdatatempFilePath,"traffic_data_temp",trafficdatatemp.class,starttime,endtime));

        return 0;


    }



    public static int ftd() throws Exception{

        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select substr(cast(EPOCH_TO_TIMESTAMP(first_recharge_time) as string),1,10) DateID\n" +
                ",first_visit_source_type channel\n" +
                ",count(uid) ftd\n" +
                "from users\n" +
                "where substr(cast(EPOCH_TO_TIMESTAMP(first_recharge_time) as string),1,10) is not null\n" +
                "group by substr(cast(EPOCH_TO_TIMESTAMP(first_recharge_time) as string),1,10),\n" +
                "first_visit_source_type  ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑
        BufferedWriter writer = new BufferedWriter(new FileWriter(ftdFilePath));

        // 执行 curl 命令
        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int databaseoutputcount = 0;

        while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
            ftd person = gson.fromJson(line, ftd.class);
            //+person.kyc_state+","+person.ekyc_state+","+person.first_recharge_time+","+person.last_recharge_time+","+person.first_order_time + "," + person.last_order_time + "," +person.first_winning_time + "," + person.first_withdraw_time+ "," +person.last_withdraw_time+","
            writer.write(person.dateid+","+person.channel+","+person.ftd);
//                        writer.write(person.uid+","+person.register_time);
            writer.newLine();
            databaseoutputcount ++;
            if(databaseoutputcount%10==0){
                System.out.println(" 目前是"+databaseoutputcount+"\n");
            }
        }
        InLog(msg.toString());
        // 等待命令执行完成,如果记录为空，是VPN没有连接
        if(databaseoutputcount==0){
            InLog("please check vpn !");
            throw new Exception("please check vpn!");
        }
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Curl command executed successfully ！");
            InLog( msg + "Curl command executed successfully ！");
        } else {
            System.out.println("Curl command failed with exit code: " + exitCode);
            InLog( msg + "Curl command failed with exit code: " + exitCode);

        }

        writer.close();

        InLog(dmlacid.loaddataitemsgeneral(mysqljdbcconn.getInstance().getConnection(),ftdFilePath,"ftd",ftd.class,null,null));



        return 0;


    }





    public static int trafficdataandftdDMLSQL(String starttime,String endtime) throws Exception{

        InLog(dmlacid.executeSQLGeneral(mysqljdbcconn.getInstance().getConnection(),"delete from traffic_data where dateid>='"+starttime+"' and dateid<'"+endtime+"';",financeJDBC));
        InLog(dmlacid.executeSQLGeneral(mysqljdbcconn.getInstance().getConnection(),"insert into traffic_data \n" +
                "(DateID,\n" +
                "Channel,\n" +
                "UV,\n" +
                "PV,\n" +
                "ftd)\n" +
                "select \n" +
                "a.DateID\n" +
                ",(case when a.Channel = 'null' then 0 else a.Channel end ) Channel \n" +
                ",a.UV\n" +
                ",a.PV\n" +
                ",(case when b.ftd is null then 0 else b.ftd end ) ftd\n" +
                "from (select * from traffic_data_temp where dateid>='"+starttime+"' and dateid<'"+endtime+"' ) a left join ftd b \n" +
                "on a.DateID = b.DateID\n" +
                "and a.Channel  = b.channel  ; " ,financeJDBC  ));
        return 0;

    }
    public static int userinfo2SQL(String starttime,String endtime) throws Exception{


        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= SELECT \n" +
                "              uid, \n" +
                "                  first_visit_source,\n" +
                "                  EPOCH_TO_TIMESTAMP(register_time ) register_time  ,\n" +
                "                   kyc_state, ekyc_state,\n" +
        "                  countries country,  \n" +
                "                  city,  \n" +
                "                  nvl(case when birthday <0 then FROM_UNIXTIME(cast(birthday/1000 as int))  else EPOCH_TO_TIMESTAMP(birthday) end, '1988-01-06 00:00:00') birthday, \n" +
                "                  substr(cast(EPOCH_TO_TIMESTAMP($update_time ) as string),1,10) update_date \n" +
                "               FROM users \n" +
                "WHERE first_visit_source is not null " +
                "and substr(cast(EPOCH_TO_TIMESTAMP($update_time ) as string),1,19)>= '"+starttime+" 00:00:00' " +
                "and substr(cast(EPOCH_TO_TIMESTAMP($update_time ) as string),1,19)< '"+endtime+" 00:00:00' ",
                "--data-urlencode", "format=json",
    };


        // 输出 逻辑
            BufferedWriter writer = new BufferedWriter(new FileWriter(userinfo2FilePath));

            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                userinfo person = gson.fromJson(line, userinfo.class);
                //+person.kyc_state+","+person.ekyc_state+","+person.first_recharge_time+","+person.last_recharge_time+","+person.first_order_time + "," + person.last_order_time + "," +person.first_winning_time + "," + person.first_withdraw_time+ "," +person.last_withdraw_time+","
                writer.write(person.uid+","+person.first_visit_source+","+person.register_time+","+person.country+","+person.city+","+person.birthday+","+person.update_date);
//                        writer.write(person.uid+","+person.register_time);
                writer.newLine();
                databaseoutputcount ++;
                if(databaseoutputcount%10==0){
                    System.out.println(" 目前是"+databaseoutputcount+"\n");
                }
            }
            InLog(msg.toString());
            // 等待命令执行完成,如果记录为空，是VPN没有连接
            if(databaseoutputcount==0){
                 InLog("please check vpn !");
                 throw new Exception("please check vpn!");
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Curl command executed successfully ！");
                InLog( msg + "Curl command executed successfully ！");
            } else {
                System.out.println("Curl command failed with exit code: " + exitCode);
                InLog( msg + "Curl command failed with exit code: " + exitCode);

            }

            writer.close();
            /** 阿里云插入限制 **/
            // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
            // 通过 excel load fail 导入
        InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),userinfo2FilePath,"userinfo",userinfo.class,starttime,endtime));
        return 0;

    }

    public static int orderwintosqlserver() {

        String starttime = timeutils.getDayStart();
        String endtime = timeutils.getNowTime();
//        String starttime = "2025-03-28 00:00:00";
//        String endtime = "2025-03-30 00:00:00";
        List<orderwintosqlserver> orderwintosqlserver = new ArrayList<>();
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= \n" +
                "select \n" +
                "zz.uid user_id,\n" +
                "abcd.lottery_type lottery,\n" +
                "abcd.order_id,\n" +
                "estimated_price  turnover,\n" +
                "substr(cast(time as string),1,19)  creation_date,\n" +
                "lottery_entries entries,\n" +
                "substr(cast(time as string),1,19) ordertime,\n" +
                "series_number series_no,\n" +
                "case when winning_amount is NULL then 'Not won' else 'won' end  winning_status,\n" +
                "case when winning_amount is NULL then 0 else winning_amount end  prize,\n" +
                "substr(cast(time as string),1,10) dateid, \n"+
                "from_unixtime(unix_timestamp()) modified_time\n"+
                "from (\n" +
                "select \n" +
                "user_id,\n" +
                "order_id,\n" +
                "lottery_type,\n" +
                "pay_type,\n" +
                "series_number,\n" +
                "lottery_entries,\n" +
                "estimated_price,\n" +
                "time\n" +
                "from  events \n" +
                "where event = 'lottery_order_result'\n" +
                "and is_success = 1\n" +
                "and lottery_type in ('Lucky Day','GOLDEN 7','MEGA SAILS','COPPER CUPS','OASIS BONANZA')\n" +
                "and time >= '"+starttime+"'\n" +
                "and time  < '"+endtime+"'\n" +
                ") abcd\n" +
                "left join (\n" +
                " select  user_id, sum(winning_amount) winning_amount ,lottery_type,order_id  \n" +
                " from events\n" +
                " where event = 'winning_detail' \n" +
                " group by order_id,user_id,lottery_type\n" +
                " ) xyz\n" +
                " on abcd.order_id = xyz.order_id \n" +
                "left join (select uid,id from users ) zz \n" +
                "on abcd.user_id  = zz.id  \n  ",
                "--data-urlencode", "format=json",
        };




        // 输出 逻辑
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(orderwintosqlserver2FilePath));

            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
                orderwintosqlserver person = gson.fromJson(line, orderwintosqlserver.class);
                orderwintosqlserver.add(person);
                writer.write(person.order_id+","+
                        person.user_id+","+
                        person.lottery+","+
                        person.series_no+","+
                        person.entries+","+
                        person.turnover+","+
                        person.creation_date+","+
                        person.prize+","+
                        person.winning_status + "," +
                        person.dateid + "," +
                        person.modified_time);
                writer.newLine();
                databaseoutputcount ++;
                if(databaseoutputcount%10==0){
                    System.out.println(" 目前是"+databaseoutputcount+"\n");
                }
            }
            InLog(msg.toString());
            // 取数失败
            if(databaseoutputcount==0){
                InLog("please check vpn !");
                throw new Exception("please check vpn!");
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Curl command executed successfully ！");
                InLog( msg + "Curl command executed successfully ！");
            } else {
                System.out.println("Curl command failed with exit code: " + exitCode);
                InLog( msg + "Curl command failed with exit code: " + exitCode);

            }

            writer.close();


            InLog(dmlacid.loaddataitemsgeneral(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),orderwintosqlserver2FilePath,"stg.incre_orders",orderwintosqlserver.class,starttime,endtime));


            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            InLog(msg + e.getMessage());
            return 0;
        }




    }







}
