package jdbc;

import au.com.bytecode.opencsv.CSVReader;
import pojp.bussinfo;
import pojp.factjobscheduler;
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
            String DelSQL = "";
            if("userinfo".equals(tablename)){
                 DelSQL = "DELETE FROM " + tablename + " WHERE update_date >= '" + startdate + "' AND update_date < '" + enddate + "'";
            }
            else
            {
                 DelSQL = "DELETE FROM " + tablename + " WHERE dateid >= '" + startdate + "' AND dateid < '" + enddate + "'";
            }
            try (Statement stmt = connection.createStatement()) {
                String sql = (startdate == null) ? "DELETE FROM " + tablename + " WHERE 1=1"
                        : DelSQL;
                stmt.executeUpdate(sql);
            }
            try (CSVReader csvReader = new CSVReader(new FileReader(tablepath));
                 PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

                String[] values;
                boolean firstLine = true; // 如果第一行是表头，可以跳过
                int batchSize = 0;
                StringBuffer useridbuffer = new StringBuffer();
                while ((values = csvReader.readNext()) != null) {
                    for (int i = 0; i < values.length; i++) {
                        pstmt.setString(i + 1, values[i].trim()); // 处理每个字段，去掉多余空格
                        if("userinfo".equals(tablename)){
                            useridbuffer.append("'"+values[0]+"',");
                        }
                        System.out.println(values.length + ": " + values[i]);
                    }

                    pstmt.addBatch();
                    batchSize++;

                    // 每 3000 行执行一次批量插入，提高效率
                    if (batchSize % 3000 == 0) {
                        if("userinfo".equals(tablename)){
                            try (Statement stmt = connection.createStatement()) {
                                String sql =  "DELETE FROM " + tablename + " WHERE uid in ("+useridbuffer.toString().substring(0,useridbuffer.toString().length()-1)+");";
                                stmt.executeUpdate(sql);
                                useridbuffer = new StringBuffer();
                            }
                        }
                        pstmt.executeBatch();
                    }
                }

                // 处理剩余数据
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


    public static <T> List<T> listTableRecord(Connection connection, String tablename, Class<T> clazz) {

        List<T> records = new ArrayList<>();

        // 基本的表名校验，防止 SQL 注入
        if (!tablename.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Invalid table name: " + tablename);
        }

        String sql = "SELECT * FROM " + tablename; // 直接使用表名，无法使用占位符

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // 获取数据库列信息，保证字段匹配
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Integer> columnMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                columnMap.put(metaData.getColumnName(i).toLowerCase(), i);
            }

            // 反射获取类字段
            Field[] fields = clazz.getDeclaredFields();

            while (rs.next()) {
                T record = clazz.getDeclaredConstructor().newInstance();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName().toLowerCase();

                    if (columnMap.containsKey(columnName)) {
                        Object columnValue = rs.getObject(columnMap.get(columnName));
                        if (columnValue != null) {
                            field.set(record, columnValue);
                        }
                    }
                }
                records.add(record);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (ReflectiveOperationException e) {
            System.err.println("Reflection error: " + e.getMessage());
        }

        return records;
    }


    /**
     * 插入 单个对象到数据库中
     * @param tablename
     * @param ob
     */
    public static void insertTableSingleRecord (Connection connection,String tablename, Object ob) {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection cannot be null");
        }

        StringBuffer logger = new StringBuffer();
        try {
            // 获取对象的类
            Class<?> clazz = ob.getClass();
            Field[] fields = clazz.getDeclaredFields();

            // 生成 SQL 语句占位符
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();

            for (Field field : fields) {
                field.setAccessible(true);
                columns.append(field.getName()).append(", ");
                placeholders.append("?, ");
            }

            // 移除末尾逗号
            if (columns.length() > 0) {
                columns.setLength(columns.length() - 2);
            }
            if (placeholders.length() > 0) {
                placeholders.setLength(placeholders.length() - 2);
            }

            // 构建 SQL 语句
            String sql = "INSERT INTO " + tablename + " (" + columns + ") VALUES (" + placeholders + ")";

            // 预编译 SQL
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                // 绑定参数
                int index = 1;
                for (Field field : fields) {
                    Object value = field.get(ob);
                    pstmt.setObject(index++, value);
                }

                // 执行 SQL 语句
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " rows inserted");
                logger.append(rowsAffected).append(" rows inserted");
            }

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            logger.append(e.getMessage()).append(" Database update failed!");
        }
    }


    public static void updateTableRecord(Connection connection, String tablename, Object ob, String primaryKeyField) {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection cannot be null");
        }

        StringBuffer logger = new StringBuffer();
        try {
            // 获取对象的类
            Class<?> clazz = ob.getClass();
            Field[] fields = clazz.getDeclaredFields();

            // 生成 SQL 语句
            StringBuilder setClause = new StringBuilder();
            String primaryKeyValue = null;

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName();
                Object value = field.get(ob);

                if (columnName.equalsIgnoreCase(primaryKeyField)) {
                    primaryKeyValue = value != null ? value.toString() : null;
                } else {
                    setClause.append(columnName).append(" = ?, ");
                }
            }

            // 检查是否获取到主键值
            if (primaryKeyValue == null) {
                throw new IllegalArgumentException("Primary key value is null, update operation aborted.");
            }

            // 移除末尾逗号
            if (setClause.length() > 0) {
                setClause.setLength(setClause.length() - 2);
            }

            // 构建 SQL 语句
            String sql = "UPDATE " + tablename + " SET " + setClause + " WHERE " + primaryKeyField + " = ?";

            // 预编译 SQL
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                int index = 1;

                // 绑定参数（更新字段）
                for (Field field : fields) {
                    if (!field.getName().equalsIgnoreCase(primaryKeyField)) {
                        Object value = field.get(ob);
                        pstmt.setObject(index++, value);
                    }
                }

                // 绑定 WHERE 主键参数
                pstmt.setObject(index, primaryKeyValue);

                // 执行 SQL 语句
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " rows updated");
                logger.append(rowsAffected).append(" rows updated");
            }

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            logger.append(e.getMessage()).append(" Database update failed!");
        }
    }




}
