package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pojp.dbconntype;

public class sqlserverjdbcconn {


    private static String vivianLink = "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-zh66nixkejeezfkd7c3mvabosy.database.fabric.microsoft.com:1433;"
            + "database=NL_Database-85d4d454-81b4-4800-a950-2fdc4becb320;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "authentication=ActiveDirectoryPassword;"
            + "user=zchai@mcorp.ae;"
            + "password=Adgjl@159357321";
    private static String generalLink = "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-dngoeut2h7lubnqjvyommvf4vq.database.fabric.microsoft.com:1433;"
            + "database={NLAndTWDatabase-b839fe70-24a3-4802-89fe-2db1618b846d};"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "authentication=ActiveDirectoryPassword;"
            + "user=zchai@mcorp.ae;"
            + "password=Adgjl@159357321";

    // 创建一个私有的静态实例，防止外部实例化
    private static HikariDataSource dataSource;

    // 私有构造函数，避免外部实例化
    private sqlserverjdbcconn() {
    }

    // 获取数据库连接池实例（懒加载，线程安全）
    public static HikariDataSource getInstance(dbconntype.sqlserverconn connstr) {
        if (dataSource == null) {
            synchronized (sqlserverjdbcconn.class) {
                if (dataSource == null) {
                    // 初始化连接池
                    HikariConfig config = new HikariConfig();
                    String connectionString = null;
                    // 设置 JDBC URL（连接字符串）
                    if(dbconntype.sqlserverconn.vivian.equals(connstr)){
                        connectionString = vivianLink;
                    } else if(dbconntype.sqlserverconn.general.equals(connstr)){
                        connectionString = generalLink;
                    }
                    config.setJdbcUrl(connectionString);

                    // 不需要设置用户名和密码，已通过连接字符串提供
                    // config.setUsername("");
                    // config.setPassword("");

                    // 可选配置：设置连接池的其他参数
                    config.setMaximumPoolSize(10); // 设置最大连接池大小
                    config.setConnectionTimeout(30000); // 设置连接超时时间（毫秒）
                    config.setIdleTimeout(600000); // 设置连接池空闲连接的超时时间（毫秒）
                    config.setMaxLifetime(1800000); // 设置连接池中连接的最大生命周期（毫秒）

                    // 创建数据源
                    dataSource = new HikariDataSource(config);
                }
            }
        }
        return dataSource;
    }

    public static void main(String[] args) {
        try (Connection connection = getInstance(dbconntype.sqlserverconn.general).getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
