package jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class mysqljdbcconn {


    private static String financeLink = "jdbc:mysql://20.174.38.36:3306/lottery_reporting" +
            "?allowLoadLocalInfile=true" +
            "&useSSL=false" +
            "&allowPublicKeyRetrieval=true" +
            "&serverTimezone=UTC" +
            "&useCompression=true" +
            "&user=Viviene" +
            "&password=VALe@1234";

    // 私有静态数据源实例，确保单例模式
    private static HikariDataSource dataSource;

    // 私有构造方法，防止外部实例化
    private mysqljdbcconn() {
    }


    // 获取 HikariCP 连接池实例
    public static HikariDataSource getInstance() {
        if (dataSource == null) {
            synchronized (mysqljdbcconn.class) {
                if (dataSource == null) {
                    HikariConfig config = new HikariConfig();
                    // 设置 JDBC URL 连接信息
                    String jdbcUrl = financeLink;
                    config.setJdbcUrl(jdbcUrl);

                    // 连接池配置参数
                    config.setMaximumPoolSize(10); // 最大连接数
                    config.setMinimumIdle(2); // 最小空闲连接数
                    config.setConnectionTimeout(30000); // 连接超时时间（毫秒）
                    config.setIdleTimeout(600000); // 空闲超时（毫秒）
                    config.setMaxLifetime(1800000); // 连接最大存活时间（毫秒）

                    // 创建 HikariCP 数据源
                    dataSource = new HikariDataSource(config);
                }
            }
        }
        return dataSource;
    }


    public static void main(String[] args) {
        try (Connection connection = getInstance().getConnection()) {
            System.out.println("MySQL Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
