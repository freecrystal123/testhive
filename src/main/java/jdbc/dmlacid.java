package jdbc;

import pojp.bussinfo;
import pojp.optdata;
import pojp.orderwin0122;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dmlacid {

    public static String loaddatafile (Connection connection,String path) throws  Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE incremental_allOrders " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(order_id,uid,investment_amount,lottery_entries,ordertime,series_number,winning_flag,winning_amount ,draw_period , dateid)";

        try (  Statement stmt = connection.createStatement())
        {
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


    public static String loadfailmonitoring(Connection connection,String path) throws Exception {
        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE fact_fail_monitoring_h " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(hour,fail_reason_count)";


        try (  Statement stmt = connection.createStatement())
        {
            String sql = "delete from fact_fail_monitoring_h where 1=1";  // 替换为你要清空的表名
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

    public static String loadfailreason(Connection connection,String path,String starttime,String endtime) throws Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE fact_login_fail_reason_d " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(dateid,login_fail_reason,number_of_user)";


        try (  Statement stmt = connection.createStatement())
        {
            String sql = "delete from fact_login_fail_reason_d where dateid between '"+starttime+" 00:00:00' and '"+endtime+" 00:00:00'";  // 替换为你要清空的表名
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



    public static String loaddataitemsgeneral(Connection connection, String tablepath, String tablename, Class<?> clazz, String startdate, String enddate) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder fieldstr = new StringBuilder();
        for (Field field : fields) {
            fieldstr.append(field.getName()).append(",");
        }
        StringBuilder logger = new StringBuilder();

        String dbProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
        boolean isMySQL = dbProductName.contains("mysql");

        if (isMySQL) {
            String query = "LOAD DATA LOCAL INFILE '"+tablepath+"' INTO TABLE "+tablename+" " +
                    "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                    "(" +
                    fieldstr.toString().substring(0, fieldstr.toString().length()-1)+
                    ")";

            try (Statement stmt = connection.createStatement()) {
                String sql = (startdate == null) ? "DELETE FROM " + tablename + " WHERE 1=1"
                        : "DELETE FROM " + tablename + " WHERE dateid >= '" + startdate + "' AND dateid <= '" + enddate + "'";
                stmt.executeUpdate(sql);
                int rowsAffected = stmt.executeUpdate(query);
                logger.append(rowsAffected).append(" rows inserted");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.append(e.getMessage()).append(" database update failed!");
            }
        } else { // SQL Server
            String fieldNames = fieldstr.substring(0, fieldstr.length() - 1);
            String placeholders = fieldNames.replaceAll("[a-zA-Z0-9_]+", "?");
            String insertSQL = "INSERT INTO " + tablename + " (" + fieldNames + ") VALUES (" + placeholders + ")";

            try (Statement stmt = connection.createStatement()) {
                String sql = (startdate == null) ? "DELETE FROM " + tablename + " WHERE 1=1"
                        : "DELETE FROM " + tablename + " WHERE dateid >= '" + startdate + "' AND dateid <= '" + enddate + "'";
                stmt.executeUpdate(sql);
            }
            try (BufferedReader br = new BufferedReader(new FileReader(tablepath));
                 PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

                String line;
                boolean firstLine = true;
                int batchSize = 0;
                while ((line = br.readLine()) != null) {
//                    if (firstLine) {
//                        firstLine = false;
//                        continue;
//                    }
                    String[] values = line.split(",");
                    for (int i = 0; i < values.length; i++) {
                        pstmt.setString(i + 1, values[i]);
                    }
                    pstmt.addBatch();
                    batchSize++;
                    if (batchSize % 1000 == 0) {
                        pstmt.executeBatch();
                    }
                }
                pstmt.executeBatch();
                logger.append("Data inserted successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                logger.append(e.getMessage()).append(" data insert failed!");
            }
        }
        return logger.toString();
    }

    public static String executeSQLGeneral(Connection connection,String dmlSQL, Properties jdbproperties) throws Exception {
        StringBuffer logger = new StringBuffer();

        try (   Statement stmt = connection.createStatement())
        {
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


    public static String loaddatafileUserInfo(Connection connection,String path) throws Exception {

        StringBuffer logger = new StringBuffer();
        String query = "LOAD DATA LOCAL INFILE '"+path+"' INTO TABLE userinfo " +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' " +
                "(uid,first_visit_source,register_time,country,city,birthday)";


        try ( Statement stmt = connection.createStatement())
        {
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

    public static String insertandupdate (Connection connection,String etldate) throws  Exception {

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

        try (  Statement stmt = connection.createStatement())
        {
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

    public static Map<String, Object> listMapColumn(String sql, Properties jdbcpro) {
        Map<String, Object> records = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(
                jdbcpro.getProperty("jdbcurl"),
                jdbcpro.getProperty("username"),
                jdbcpro.getProperty("password"));
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                    String columnValue1 = rs.getObject(1).toString();
                    Object columnValue2 = rs.getObject(2);
                records.put(columnValue1, columnValue2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // 生产环境应使用日志记录，不要直接打印
        }

        return records;
    }


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


}
