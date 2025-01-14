package jdbc;
import pojp.bussinfo;
import pojp.optdata;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
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


