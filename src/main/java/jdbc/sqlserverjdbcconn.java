package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pojp.dbconntype;

public class sqlserverjdbcconn {


    private static String vivianLink = "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-zh66nixkejeezfkd7c3mvabosy.database.fabric.microsoft.com:1433;"
            + "database={NL_Database-85d4d454-81b4-4800-a950-2fdc4becb320};"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "authentication=ActiveDirectoryPassword;"
            + "user=zchai@mcorp.ae;"
            + "password=Adgjl@159357321";
    private static String vivianLink1 =  "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-zh66nixkejeezfkd7c3mvabosy.datawarehouse.fabric.microsoft.com:1433;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "hostNameInCertificate=*.datawarehouse.fabric.microsoft.com;"
            + "loginTimeout=30;"
            + "user=zchai@mcorp.ae;"
            + "password=Adgjl@159357321";

    private static String generalLink = "jdbc:sqlserver://hco65xnsg6dulio4raop7psqwe-dngoeut2h7lubnqjvyommvf4vq.database.fabric.microsoft.com:1433;"
            + "database={NLAndTWDatabase-b839fe70-24a3-4802-89fe-2db1618b846d};"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "authentication=ActiveDirectoryPassword;"
            + "user=zchai@mcorp.ae;"
            + "password=Adgjl@159357321";
    // 创建两个静态数据源实例
    private static HikariDataSource generaldataSource;
    private static HikariDataSource ViviandataSource;

    // 私有构造函数，避免外部实例化
    private sqlserverjdbcconn() {}

    // 获取数据库连接池实例
    public static HikariDataSource getInstance(dbconntype.sqlserverconn connType) {
        if (connType == dbconntype.sqlserverconn.vivian) {
            return getVivianDataSource();
        } else if (connType == dbconntype.sqlserverconn.general) {
            return getGeneralDataSource();
        }
        throw new IllegalArgumentException("Invalid connection type");
    }

    // 获取 Vivian 数据库的数据源
    private static synchronized HikariDataSource getVivianDataSource() {
        if (ViviandataSource != null) {
            if (!isCorrectDatabase(ViviandataSource, "NL_Database-85d4d454-81b4-4800-a950-2fdc4becb320")) {
                ViviandataSource.close();
                ViviandataSource = null;
            }
        }
        if (ViviandataSource == null) {
            ViviandataSource = createDataSource(vivianLink);
        }
        return ViviandataSource;
    }

    // 获取 General 数据库的数据源
    private static synchronized HikariDataSource getGeneralDataSource() {
        if (generaldataSource != null) {
            if (!isCorrectDatabase(generaldataSource, "NLAndTWDatabase-b839fe70-24a3-4802-89fe-2db1618b846d")) {
                generaldataSource.close();
                generaldataSource = null;
            }
        }
        if (generaldataSource == null) {
            generaldataSource = createDataSource(generalLink);
        }
        return generaldataSource;
    }

    // 创建 Hikari 数据源
    private static HikariDataSource createDataSource(String jdbcUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setMaximumPoolSize(30);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(20000);

        return new HikariDataSource(config);
    }

    // 检查当前数据源是否连接到指定数据库
    private static boolean isCorrectDatabase(HikariDataSource dataSource, String expectedDatabase) {
        try {
            String url = dataSource.getConnection().getMetaData().getURL();
            for (String param : url.split(";")) {
                if (param.contains("databaseName")) {
                    return param.split("=")[1].equals(expectedDatabase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static void main(String[] args) {
        try (Connection connection = getInstance(dbconntype.sqlserverconn.general).getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
