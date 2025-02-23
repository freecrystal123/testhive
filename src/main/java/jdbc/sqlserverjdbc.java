package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class sqlserverjdbc {

    // 创建一个私有的静态实例，防止外部实例化
    private static HikariDataSource dataSource;

    // 私有构造函数，避免外部实例化
    private sqlserverjdbc() {
    }

    // 获取数据库连接池实例（懒加载，线程安全）
    public static HikariDataSource getInstance() {
        if (dataSource == null) {
            synchronized (sqlserverjdbc.class) {
                if (dataSource == null) {
                    // 初始化连接池
                    HikariConfig config = new HikariConfig();

                    // 设置 JDBC URL（连接字符串）
                    String connectionString = "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-zh66nixkejeezfkd7c3mvabosy.database.fabric.microsoft.com:1433;"
                            + "database=NL_Database-85d4d454-81b4-4800-a950-2fdc4becb320;"
                            + "encrypt=true;"
                            + "trustServerCertificate=false;"
                            + "authentication=ActiveDirectoryInteractive";

                    config.setJdbcUrl(connectionString);
                    config.setUsername(""); // 不需要设置用户名
                    config.setPassword(""); // 不需要设置密码

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

    // 获取数据库连接并执行操作
    public static Connection getConnection() throws SQLException {
        return getInstance().getConnection();
    }

    // 关闭数据源
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static void main(String[] args) {
        try (Connection connection = sqlserverjdbc.getConnection()) {
            System.out.println("数据库连接成功！");
            // 在此处执行 SQL 查询或其他数据库操作
        } catch (SQLException e) {
            System.out.println("数据库连接失败： " + e.getMessage());
        } finally {
            sqlserverjdbc.shutdown();  // 关闭连接池
        }
    }
}

