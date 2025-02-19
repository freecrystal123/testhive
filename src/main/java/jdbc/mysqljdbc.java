package jdbc;

import pojp.bussinfo;
import pojp.optdata;
import pojp.orderwin0122;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mysqljdbc {

    private static String NginxCPU =  "(?=.*nginx)(?=.*CPU)";
    private static String NginxDisk = "Nginx Disk";
    private static String NginxServer = "Nginx Server";
    private static String NginxMemory = "Nginx Memory";
    private static String WebCPU =  "(?=.*Web)(?=.*CPU)";
    private static String WebMemory = "Web Memory";
    private static String WebDisk = "Web Disk";
    private static String WebServer = "Web Server";
    private static String BussinesInfo = "Buss Info";
//
//    public static void main(String[] args) {
//        // 要插入的数据
//        String timeStr = "2024-12-13 00:00:00";
//        double nginx_cpu = 30.3;
//        java.sql.Date date =  null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 定义日期格式
//
//        try {
//            // 将字符串转换为日期对象
//            date = new Date(sdf.parse(timeStr).getTime());
//            System.out.println("Converted Date: " + date);
//        } catch (Exception e) {
//            System.out.println("Invalid date format.");
//        }
//
//
//        // SQL 插入语句
//        String insertSQL = "INSERT INTO nginx_cpu (time, nginx_cpu) VALUES (?, ?)";
//        try{
//
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rawdata?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "123123");
//            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
//
//            // 设置参数
//            preparedStatement.setDate(1, date);
//            preparedStatement.setDouble(2, nginx_cpu);
//
//            // 执行插入操作
//            int rowsAffected = preparedStatement.executeUpdate();
//            System.out.println("Inserted " + rowsAffected + " row(s) into the users table.");
//        } catch(SQLException e){
//            e.printStackTrace();
//        }
//    }

    public static String loaddatafile (String path) throws  Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE incremental_allOrders " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(order_id,uid,investment_amount,lottery_entries,ordertime,series_number,winning_flag,winning_amount ,draw_period , dateid)";

        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://47.99.103.128:3306/Lottery?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true", "root", "1234");
             Statement stmt = connection.createStatement()) {
            String sql = "delete from incremental_allOrders where 1=1";  // 替换为你要清空的表名
            stmt.executeUpdate(sql);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " rows inserted");
            logger.append(rowsAffected + " rows inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
        }
    return logger.toString();
    }



    public static String loadregister(String path,String starttime,String endtime) throws Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE daily_register " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(dateid,register)";

        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true", "Viviene", "VALe@1234");
              Statement stmt = connection.createStatement()) {
            String sql = "delete from daily_register where dateid between '"+starttime+" 00:00:00' and '"+endtime+" 00:00:00'";  // 替换为你要清空的表名
            stmt.executeUpdate(sql);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " rows inserted");
            logger.append(rowsAffected + " rows inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append("database update failed !");
        }
        return logger.toString();

    }



    public static String loaddataitemsgeneral(String tablepath,String tablename , Class clazz,String startdate,Properties jdbcpro) throws Exception {

        Field[] fields =  clazz.getDeclaredFields();
        StringBuffer fieldstr = new StringBuffer();
        for(Field field:fields){
            fieldstr.append(field.getName()+",");
        }
        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+tablepath+"' INTO TABLE "+tablename+" " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(" +
                fieldstr.toString().substring(0, fieldstr.toString().length()-1)+
                ")";

        try ( Connection connection = DriverManager.getConnection(jdbcpro.get("jdbcurl").toString(),jdbcpro.get("username").toString() , jdbcpro.get("password").toString() );
              Statement stmt = connection.createStatement()) {
            String sql = "";
            if(startdate==null) {
                 sql = "delete from "+tablename+" where 1=1";  // 替换为你要清空的表名
            } else {
                 sql = "delete from "+tablename+" where dateid > '"+startdate+"'";  // 替换为你要清空的表名
            }
            stmt.executeUpdate(sql);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " rows inserted");
            logger.append(rowsAffected + " rows inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append("database update failed !");
        }
        return logger.toString();
    }

    public static String executeSQLGeneral(String dmlSQL, Properties jdbproperties) throws Exception {
        StringBuffer logger = new StringBuffer();

        try ( Connection connection = DriverManager.getConnection(jdbproperties.getProperty("jdbcurl"),jdbproperties.getProperty("username"), jdbproperties.getProperty("passward"));
              Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(dmlSQL);
            System.out.println(rowsAffected + " rows updated");
            logger.append(rowsAffected + " rows updated");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append("database update failed !");
        }
        return logger.toString();


    }


    public static String loaddatafileUserInfo(String path) throws Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE userinfo " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(uid,first_visit_source,register_time,country,city,birthday)";


        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true", "Viviene", "VALe@1234");
              Statement stmt = connection.createStatement()) {
            String sql = "delete from userinfo where 1=1";  // 替换为你要清空的表名
            stmt.executeUpdate(sql);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " rows inserted");
            logger.append(rowsAffected + " rows inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append("database update failed !");
        }
        return logger.toString();


    }

    public static String insertandupdate (String etldate) throws  Exception {

        StringBuffer logger = new StringBuffer();
        String query = "INSERT INTO Lottery.fact_allOrders (\n" +
                "order_id,    \n" +
                "uid, \n" +
                "    investment_amount,\n" +
                "    lottery_entries,\n" +
                "    ordertime,\n" +
                "    series_number,\n" +
                "    winning_flag,\n" +
                "    winning_amount,\n" +
                "    Draw_Period,\n" +
                "    DateID\n" +
                ") \n" +
                "SELECT \n" +
                "    d.order_id,\n" +
                "    d.uid,\n" +
                "    d.investment_amount,\n" +
                "    d.lottery_entries,\n" +
                "    d.ordertime,\n" +
                "    d.series_number,\n" +
                "    d.winning_flag,\n" +
                "    d.winning_amount,\n" +
                "    d.Draw_Period,\n" +
                "    d.DateID\n" +
                "FROM Lottery.incremental_allOrders AS d\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "    uid = VALUES(uid),\n" +
                "    investment_amount = VALUES(investment_amount),\n" +
                "    lottery_entries= VALUES(lottery_entries) ,\n" +
                "    ordertime = VALUES(ordertime),\n" +
                "    series_number = VALUES(series_number),\n" +
                "    winning_flag = VALUES(winning_flag),\n" +
                "    winning_amount = VALUES(winning_amount),\n" +
                "    Draw_Period= VALUES(Draw_Period),\n" +
                "    DateID = VALUES(DateID)";

        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://47.99.103.128:3306/Lottery?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true", "root", "1234");
              Statement stmt = connection.createStatement()) {
            // 将字符串解析为 LocalDate 对象
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(etldate, inputFormatter);

            // 定义输出格式，不带“-”
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            // 格式化日期
            String formattedDate = date.format(outputFormatter);

            String sql = "create table Lottery.fact_allOrders_bak_"+ formattedDate +" as select * from Lottery.fact_allOrders";  // 新增备份表
            stmt.executeUpdate(sql);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " rows updated");
            logger.append(rowsAffected + " rows updated");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append("database update failed !");
        }
        return logger.toString();

    }


    public  static void insertincremental_allOrdersTable(List<orderwin0122> orderwin0122s)  throws  Exception{
            Connection connection = DriverManager.getConnection("jdbc:mysql://47.99.103.128:3306/Lottery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true", "root", "1234");

            // 初始化操作
           // 执行 TRUNCATE TABLE SQL 命令
//           String sql = "delete from incremental_allOrders where 1=1";  // 替换为你要清空的表名
           Statement stmt = connection.createStatement();
//           stmt.executeUpdate(sql);

            connection.setAutoCommit(false);
           // 插入操作
            String insertSQL = "INSERT INTO incremental_allOrders (uid, " +
                "order_id," +
                "investment_amount," +
                "lottery_entries," +
                "ordertime," +
                "series_number," +
                "winning_flag," +
                "winning_amount," +
                "draw_period," +
                "DateID) VALUES (?,?,?,?,?,?,?,?,?,?);";

            int count = 0;
           PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            for (orderwin0122 data : orderwin0122s) {

            count ++;


            // 设置参数
            preparedStatement.setString(1,String.valueOf(data.uid));
            preparedStatement.setString(2,String.valueOf(data.order_id));
            preparedStatement.setDouble(3,data.investment_amount);
            preparedStatement.setInt(4,data.lottery_entries);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.sql.Timestamp timestamp =  new java.sql.Timestamp(sdf.parse(data.ordertime).getTime());

            // 将 Date 转换为 Timestamp

            preparedStatement.setTimestamp(5,timestamp);
            preparedStatement.setInt(6,data.series_number);
            preparedStatement.setString(7,data.winning_flag);

            preparedStatement.setDouble(8,data.winning_amount);
            preparedStatement.setInt(9,data.draw_period);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date date1 = new java.sql.Date(sdf1.parse(data.dateid).getTime());
            preparedStatement.setDate(10,date1);


            // 将插入操作添加到批处理中
                preparedStatement.addBatch();
            if(count%1000==0){
                preparedStatement.executeBatch();
                // 清空批处理队列
                preparedStatement.clearBatch();
            } else if(orderwin0122s.size()-1==count){
                preparedStatement.executeBatch();
                // 清空批处理队列
                preparedStatement.clearBatch();
            }

            System.out.println("Inserted " + count + " row(s) into the incremental_allOrders table.");

        }
        // 提交事务
        connection.commit();


    }



//
//    public static <T> int insertlistTableRecord(String tablename , Properties jdbcpro,List<T>  clazz){
//
//        List<T> records = new ArrayList<>();
//        try (Connection connection = DriverManager.getConnection(
//                jdbcpro.get("jdbcurl").toString(),
//                jdbcpro.get("username").toString(),
//                jdbcpro.get("password").toString());
//             Statement stmt = connection.createStatement()) {
//
//            // Constructing the SELECT SQL query
//            String sql = "SELECT * FROM " + tablename;
//
//            // Execute the SELECT query
//            ResultSet rs = stmt.executeQuery(sql);
//
//            // Get the fields of the class
//            Field[] fields = clazz.getDeclaredFields();
//
//            // Process the result set
//            while (rs.next()) {
//                // Create an instance of the class using reflection
//                T record =  clazz.getDeclaredConstructor().newInstance();
//
//                // Map each field in the ResultSet to the corresponding field in the class
//                for (Field field : fields) {
//                    field.setAccessible(true); // Allow access to private fields
//                    String columnName = field.getName();
//                    Object columnValue = rs.getObject(columnName); // Get the value from the ResultSet
//
//                    // Set the value to the field using reflection
//                    if (columnValue != null) {
//                        field.set(record, columnValue);
//                    }
//                }
//
//                // Add the populated object to the list
//                records.add(record);
//            }
//
//        } catch (SQLException | ReflectiveOperationException e) {
//            e.printStackTrace();
//            // Handle exception properly in production code
//        }
//
//        return records;
//    }
//
//


    public static <T> List<T> listTableRecord(String tablename , Properties jdbcpro,Class<T> clazz){

        List<T> records = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                jdbcpro.get("jdbcurl").toString(),
                jdbcpro.get("username").toString(),
                jdbcpro.get("password").toString());
             Statement stmt = connection.createStatement()) {

            // Constructing the SELECT SQL query
            String sql = "SELECT * FROM " + tablename;

            // Execute the SELECT query
            ResultSet rs = stmt.executeQuery(sql);

            // Get the fields of the class
            Field[] fields = clazz.getDeclaredFields();

            // Process the result set
            while (rs.next()) {
                // Create an instance of the class using reflection
                T record =  clazz.getDeclaredConstructor().newInstance();

                // Map each field in the ResultSet to the corresponding field in the class
                for (Field field : fields) {
                    field.setAccessible(true); // Allow access to private fields
                    String columnName = field.getName();
                    Object columnValue = rs.getObject(columnName); // Get the value from the ResultSet

                    // Set the value to the field using reflection
                    if (columnValue != null) {
                        field.set(record, columnValue);
                    }
                }

                // Add the populated object to the list
                records.add(record);
            }

        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
            // Handle exception properly in production code
        }

        return records;
    }


    /**
     * 插入 单个对象到数据库中
     * @param tablename
     * @param ob
     * @param jdbcpro
     */
    public static void insertTableSingleRecord (String tablename, Object ob, Properties jdbcpro) {


        StringBuffer logger = new StringBuffer();
        try (Connection connection = DriverManager.getConnection(jdbcpro.get("jdbcurl").toString(),
                jdbcpro.get("username").toString(),
                jdbcpro.get("password").toString());
             Statement stmt = connection.createStatement()) {

            // 获取 ob 对象的类
            Class<?> clazz = ob.getClass();

            // 获取所有字段
            Field[] fields = clazz.getDeclaredFields();

            // 生成字段名称和字段值的字符串
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Field field : fields) {
                field.setAccessible(true); // 设置可访问私有字段
                String columnName = field.getName(); // 获取字段名
                Object value = field.get(ob); // 获取字段值

                // 处理字段名和字段值
                columns.append(columnName).append(", ");
                values.append("'").append(value != null ? value.toString() : "").append("', ");
            }

            // 去掉最后一个逗号和空格
            if (columns.length() > 0) {
                columns.setLength(columns.length() - 2);
            }
            if (values.length() > 0) {
                values.setLength(values.length() - 2);
            }

            // 构建插入的 SQL 语句
            String sql = "INSERT INTO " + tablename + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";

            // 执行插入操作
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println(rowsAffected + " rows inserted");
            logger.append(rowsAffected + " rows inserted");

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            logger.append(e.getMessage());
            logger.append(" Database update failed!");
        }


    }


    public static void insertTable(String cvsName, List<optdata> optdatas, List<bussinfo> bussinfos) {


        try {


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rawdata?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "123123");
            Pattern NginxCPUP = Pattern.compile(NginxCPU);
            Matcher matcherNginxCPU = NginxCPUP.matcher(cvsName);

            Pattern WebCPUP = Pattern.compile(WebCPU);
            Matcher matcherWebCPU = WebCPUP.matcher(cvsName);



            if(true){
                if (matcherNginxCPU.find()) {
                    for (optdata data : optdatas) {

                        Double nginx_cpu = data.data;
                        String insertSQL = "INSERT INTO nginx_cpu (time, nginx_cpu) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, nginx_cpu);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the nginx_cpu table.");

                    }


                } else if (cvsName.contains(NginxDisk)) {

                    for (optdata data : optdatas) {

                        Double NginxDisk = data.data;
                        String insertSQL = "INSERT INTO nginx_disk (time, nginx_disk) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, NginxDisk);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the nginx_disk table.");

                    }


                } else if (cvsName.contains(NginxServer)) {


                    for (optdata data : optdatas) {

                        Double NginxServer = data.data;
                        String insertSQL = "INSERT INTO nginx_server (time, nginx_server) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, NginxServer);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the nginx_server table.");

                    }


                } else if (cvsName.contains(NginxMemory)) {

                    for (optdata data : optdatas) {

                        Double NginxSystem = data.data;
                        String insertSQL = "INSERT INTO nginx_memory (time, nginx_memory) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, NginxSystem);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the nginx_system table.");

                    }


                }  else if (matcherWebCPU.find()) {

                    for (optdata data : optdatas) {

                        Double WebCPU = data.data;
                        String insertSQL = "INSERT INTO web_cpu (time, web_cpu) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, WebCPU);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the web_cpu table.");

                    }



                }  else if (cvsName.contains(WebMemory)) {

                    for (optdata data : optdatas) {

                        Double WebSystem = data.data;
                        String insertSQL = "INSERT INTO web_memory (time, web_memory VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, WebSystem);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the web_memory table.");

                    }



                }  else if (cvsName.contains(WebDisk)) {

                    for (optdata data : optdatas) {

                        Double WebDisk = data.data;
                        String insertSQL = "INSERT INTO web_disk (time, web_disk) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, WebDisk);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the web_disk table.");

                    }



                }  else if (cvsName.contains(WebServer)) {

                    for (optdata data : optdatas) {

                        Double WebServer = data.data;
                        String insertSQL = "INSERT INTO web_server (time, web_server) VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, WebServer);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the web_server table.");

                    }

                } else if (cvsName.contains(BussinesInfo)){
                    for (bussinfo data : bussinfos) {

                        Date time = new Date(data.Time.getTime());
                        Integer register_num = data.register_num;
                        Integer recharger_num = data.recharger_num;
                        Integer lottery_order_num = data.lottery_order_num;
                        Integer login_result = data.login_result_num;
                        Integer pageview_num = data.pageview_num;
                        String insertSQL = "INSERT INTO user_basic_info (date, register_num,recharger_num,lottery_order_num,login_result_num,pageview_num) VALUES (?,?,?,?,?,?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setDate(1,time);
                        preparedStatement.setInt(2, register_num);
                        preparedStatement.setInt(3, recharger_num);
                        preparedStatement.setInt(4, lottery_order_num);
                        preparedStatement.setInt(5, login_result);
                        preparedStatement.setInt(6, pageview_num);


                        // 执行插入操作
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the user_basic_info  table.");

                    }
                }
            } else {
                if(cvsName.contains(WebMemory)){

                    for (optdata data : optdatas) {

                        Double WebMemory = data.data;
                        String insertSQL = "INSERT INTO web_memory (time, web_memory VALUES (?, ?);";

                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        // 设置参数
                        preparedStatement.setTimestamp(1,new Timestamp(data.time.getTime()));
                        preparedStatement.setDouble(2, WebMemory);


                        // 执行插入操作
                        System.out.println("WebMemory"+WebMemory);
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Inserted " + rowsAffected + " row(s) into the web_memory table.");

                    }

                }

            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}


