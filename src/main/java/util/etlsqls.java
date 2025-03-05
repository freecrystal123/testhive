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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class etlsqls {
    // 创建 GsonBuilder 并注册自定义的 JsonDeserializer

    // 创建自定义日期格式
    static String  os = System.getProperty("os.name").toLowerCase();


    // 定义JDBC 数据库连接
    static Properties financeJDBC = null;
    static Properties alibabaJDBC = null;
    static {
        financeJDBC = new Properties();
        financeJDBC.put("jdbcurl", "jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        financeJDBC.put("username","Viviene");
        financeJDBC.put("password","VALe@1234");


        alibabaJDBC = new Properties();
        alibabaJDBC.put("jdbcurl", "jdbc:mysql://47.99.103.128:3306/Lottery?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        alibabaJDBC.put("username","root");
        alibabaJDBC.put("password","1234");

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
                throw new JsonParseException("Unparseable date: " + json.getAsString(), e);
            }
        }
    };

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, dateDeserializer)
            .create();


    // path
    public static String macbasepath = "/Users/zhihuachai/Downloads/";
    public static String winbasepath = "C:/Users/VanAnhLe/Documents/1-Data/dailyexport/";
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
    public static String output2FilePath = basepath+"output2FilePath.csv";

    public static String trafficdatatempFilePath = basepath+"trafficdatatempFilePath.csv";

    public static String ftdFilePath = basepath+"ftdFilePath.csv";

    public static String userinfo2FilePath  = basepath+"userinfo2FilePath.csv";

    public static String newregister2FilePath  = basepath+"newregisteredusersFilePath.csv";


    public static String rgusersstatics2FilePath  = basepath+"rgusersstatics2FilePath.csv";


    public static String rgdispositedlimitselftimeout2FilePath =  basepath +"rgdispositedlimitselftimeout2FilePath.csv";



    public static StringBuffer msg = new StringBuffer();
    public static StringBuffer logger = new StringBuffer();



    public static void main(String[] args) throws Exception{

        rgusersstatics();
//        rgdispositedlimitselftimeout();

    }
    public static String logs1 ;
    public static void InLog(String logs){
        logger.append(logs+"\n");
        logs1 = logger.toString();
    }
    public static String getLog(){
        return logs1;
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




    /* 直接调用*/

    public static int rgusersstatics(  ) throws Exception{

        // 获取昨天的日期
        LocalDate yesterday = LocalDate.now().minusDays(0);


        // 获取昨天的日期
        LocalDate lastMonthday = LocalDate.now().minusDays(2);

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将昨天的日期格式化为字符串
        String yesterformattedDate = yesterday.format(formatter);
        String lastmonthformattedDate = lastMonthday.format(formatter);
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select logindate dateid,loginusers logged_users,bettingusers betting_users from (\n" +
                "select substr(cast(time as string),1,10) logindate ,count(distinct uid) loginusers from users join (\n" +
                "select \n" +
                "order_id,\n" +
                "lottery_type,\n" +
                "pay_type,\n" +
                "time,\n" +
                "user_id\n" +
                "from  events \n" +
                "where event= 'login_result'\n" +
                "and is_success = 1" +
                "and  substr(cast(time as string),1,10) >= '"+lastmonthformattedDate+"' and substr(cast(time as string),1,10)<='"+yesterformattedDate +"'\n"+
                " ) aa on \n" +
                "users.id = aa.user_id \n" +
                "group by substr(cast(time as string),1,10) ) aa\n" +
                "join (\n" +
                "select substr(cast(time as string),1,10) bettingdate ,count(distinct uid) bettingusers from users join (\n" +
                "select \n" +
                "order_id,\n" +
                "lottery_type,\n" +
                "pay_type,\n" +
                "time,\n" +
                "user_id\n" +
                "from  events \n" +
                "where event = 'lottery_order_result'\n" +
                "and is_success = 1\n" +
                "and  substr(cast(time as string),1,10) >= '"+lastmonthformattedDate+"' and substr(cast(time as string),1,10)<='"+yesterformattedDate +"'\n"+
                ") aa on \n" +
                "users.id = aa.user_id \n" +
                "group by substr(cast(time as string),1,10)\n" +
                ") bb on aa.logindate = bb.bettingdate \n" +
                "order by aa.logindate desc  ",
                "--data-urlencode", "format=json",
                "--insecure https://data.admin-uaenl.ae",
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
            System.out.println(line);
//            userbussinessinfo person = gson.fromJson(line, userbussinessinfo.class);
//            writer.write(person.dateid+","+person.logged_users+","+person.betting_users+",0,0");
//            writer.newLine();
//            databaseoutputcount ++;
//            if(databaseoutputcount%10==0){
//                System.out.println(" 目前是"+databaseoutputcount+"\n");
//            }
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




    public static int newregisteredusers( String starttime,String endtime ) throws Exception{


        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select substr(cast(maxtime as String),1,10) DateID,count(uid) register from users join (\n" +
                "select max(time) maxtime  ,user_id  from events \n" +
                "where event = 'register_new'\n" +
                "group by user_id ) aa on \n" +
                "users.id = aa.user_id\n" +
                "where maxtime > '"+starttime+" 00:00:00' \n" +
                "and maxtime < '"+endtime+" 00:00:00' group by  substr(cast(maxtime as String),1,10) ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑

            BufferedWriter writer = new BufferedWriter(new FileWriter(newregister2FilePath));

            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                register person = gson.fromJson(line, register.class);
                writer.write(person.dateid+","+person.register);
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
            InLog("newregister2FilePath:"+newregister2FilePath);
            InLog(dmlacid.loadregister(mysqljdbcconn.getInstance().getConnection(),newregister2FilePath,starttime,endtime));


            return 0;
    }



    public static int traffic_data_temp( String starttime,String endtime ) throws Exception{

        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= select \n" +
                "datadate DateID,first_visit_source_type Channel,\n" +
                "count(distinct user_id) UV,\n" +
                "count(1) PV \n" +
                " from users join ( \n" +
                "select substr(cast (time as string),1,10) datadate\n" +
                ",user_id\n" +
                "from events\n" +
                "where event = '$pageview' and time >= '"+starttime+" 00:00:00' and time <='" + endtime +"' \n" +
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

        InLog(dmlacid.loaddataitemsgeneral(mysqljdbcconn.getInstance().getConnection(),trafficdatatempFilePath,"traffic_data_temp",trafficdatatemp.class,starttime,endtime));

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


    public static int userbussinessinfoDMLSQL() throws Exception{

        InLog(dmlacid.executeSQLGeneral(mysqljdbcconn.getInstance().getConnection(),"delete from fact_user_bussinessinfo_d where 1=1;",alibabaJDBC));

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
    public static int userinfo2SQL() throws Exception{


        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= SELECT \n" +
                "              uid, \n" +
                "                  first_visit_source,\n" +
                "                  EPOCH_TO_TIMESTAMP(register_time ) register_time  ,\n" +
                "                   kyc_state, ekyc_state,\n" +
                "                  case when first_recharge_time is null then '1970-01-01 00:00:00' else substr(cast(EPOCH_TO_TIMESTAMP(first_recharge_time) as string),1,19) end first_recharge_time , \n" +
                "                  case when last_recharge_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(last_recharge_time ) as string),1,19) end last_recharge_time  ,\n" +
                "                  case when first_order_time  is null then '1970-01-01 00:00:00' else substr(cast( EPOCH_TO_TIMESTAMP(first_order_time) as string),1,19) end first_order_time,\n" +
                "                  case when last_order_time  is null then '1970-01-01 00:00:00' else   substr(cast(EPOCH_TO_TIMESTAMP( last_order_time)  as string),1,19) end last_order_time,\n" +
                "                  case when first_winning_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(first_winning_time)  as string),1,19) end first_winning_time,\n" +
                "                  case when first_withdraw_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(first_withdraw_time) as string),1,19) end first_withdraw_time  ,\n" +
                "                  case when last_withdraw_time  is null then '1970-01-01 00:00:00' else    substr(cast(EPOCH_TO_TIMESTAMP(last_withdraw_time ) as string),1,19) end last_withdraw_time,  \n" +
                "                  countries country,  \n" +
                "                  city,  \n" +
                "                  EPOCH_TO_TIMESTAMP(birthday/1000) birthday  \n" +
                "               FROM users \n" +
                "WHERE first_visit_source is not null  ",
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
                writer.write(person.uid+","+person.first_visit_source+","+person.register_time+","+person.country+","+person.city+","+person.birthday);
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
            InLog(dmlacid.loaddatafileUserInfo(mysqljdbcconn.getInstance().getConnection(),userinfo2FilePath));

        return 0;

    }

    public static int output2SQL(String starttime,String endtime) {


        List<orderwin0122> orderwin0122s = new ArrayList<>();
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= \n" +
                "select \n" +
                "zz.uid,\n" +
                "abcd.lottery_type,\n" +
                "abcd.order_id,\n" +
                "estimated_price investment_amount,\n" +
                "lottery_entries,\n" +
                "substr(cast(time as string),1,19) ordertime,\n" +
                "series_number,\n" +
                "case when winning_amount is NULL then 'Not won' else 'won' end winning_flag,\n" +
                "case when winning_amount is NULL then 0 else winning_amount end winning_amount,\n" +
                "Draw_series draw_period,\n" +
                "substr(cast(time as string),1,10) DateID\n"+
                "from (\n" +
                "select \n" +
                "user_id,\n" +
                "order_id,\n" +
                "lottery_type,\n" +
                "pay_type,\n" +
                "series_number,\n" +
                "lottery_entries,\n" +
                "estimated_price,\n" +
                "time,\n" +
                "case when lottery_type='Lucky Day' and time<'2024-12-14 19:00:00' then '241214' \n" +
                "     when lottery_type !='Lucky Day' and time<'2024-12-14 19:00:00' then '241214' \n" +
                "     when  lottery_type ='Lucky Day' and  time>'2024-12-14 21:00:00' and time< '2024-12-28 19:00:00'  then '241228' \n" +
                "     when  lottery_type !='Lucky Day' and  time>'2024-12-14 19:00:00' and time< '2024-12-28 19:00:00'  then '241228'   \n" +
                "     when  lottery_type ='Lucky Day' and time>'2024-12-28 21:00:00' and time< '2025-01-11 19:00:00' then '250111' \n" +
                "     when  lottery_type !='Lucky Day' and time>'2024-12-28 19:00:00' and time< '2025-01-11 19:00:00' then '250111' \n" +
                "     when  lottery_type ='Lucky Day'  and time>'2025-01-11 21:00:00'  and time< '2025-01-25 19:00:00' then '250125' \n" +
                "     when  lottery_type !='Lucky Day'  and time>'2025-01-11 19:00:00'  and time< '2025-01-25 19:00:00' then '250125' \n" +
                "     when  lottery_type ='Lucky Day' and  time>'2025-01-25 21:00:00'  and time< '2025-02-08 19:00:00' then '250208' \n" +
                "       when  lottery_type !='Lucky Day' and  time>'2025-01-25 19:00:00'  and time< '2025-02-08 19:00:00' then '250208' \n" +
                "     end draw_series\n" +
                "from  events \n" +
                "where event = 'lottery_order_result'\n" +
                "and is_success = 1\n" +
                "and lottery_type in ('Lucky Day','GOLDEN 7','MEGA SAILS','COPPER CUPS','OASIS BONANZA')\n" +
                "and time >= '"+starttime+" 00:00:00'\n" +
                "and time  < '"+endtime+" 00:00:00'\n" +
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(output2FilePath));
//            writer.write("uid,lottery_type,order_id,investment_amount,lottery_entries,ordertime,series_number,winning_flag,winning_amount,draw_period,dateid");
//            writer.newLine();

            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                orderwin0122 person = gson.fromJson(line, orderwin0122.class);
                orderwin0122s.add(person);
                writer.write(person.order_id+","+person.uid+","+person.investment_amount+","+person.lottery_entries+","+person.ordertime+","+person.series_number+","+person.winning_flag+","+person.winning_amount + "," + person.draw_period + "," + person.dateid);
                writer.newLine();
                databaseoutputcount ++;
                if(databaseoutputcount%10==0){
                    System.out.println(" 目前是"+databaseoutputcount+"\n");
//                    msg.append(" 目前是"+databaseoutputcount+"\n");
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
            /** 阿里云插入限制 **/
            // 开始插入操作
//            mysqljdbc.insertincremental_allOrdersTable(orderwin0122s);
            // 通过 excel load fail 导入
            InLog(dmlacid.loaddatafile(mysqljdbcconn.getInstance().getConnection(),output2FilePath));

            // 通过 创建备份表和 更新全量表

            InLog(dmlacid.insertandupdate(mysqljdbcconn.getInstance().getConnection(),endtime));
            // 等待命令执行完成

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            InLog(msg + e.getMessage());
            return 0;
        }




    }







}
