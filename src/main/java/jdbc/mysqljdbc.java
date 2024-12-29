package jdbc;
import pojp.optdata;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class mysqljdbc {

    private static String NginxCPU = "Nginx CPU";
    private static String NginxDisk = "Nginx Disk";
    private static String NginxServer = "Nginx Server";
    private static String NginxMemory = "Nginx Memory";
    private static String WebCPU = "Web CPU";
    private static String WebMemory = "Web Memory";
    private static String WebDisk = "Web Disk";
    private static String WebServer = "Web Server";
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

    public static void insertTable(String cvsName, List<optdata> optdatas) {


        try {


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rawdata?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "123123");

            if (cvsName.contains(NginxCPU)) {
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


            }  else if (cvsName.contains(WebCPU)) {



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

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}


