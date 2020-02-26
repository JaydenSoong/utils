package com.soong.utils.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 基于 Druid 数据库连接池的操作 JDBC 的工具类，提供获取数据库连接、获取数据库连接池、释放资源的方法。
 * 使用该类需要提供一个 druid.properties 的配置文件。
 * 将该文件放在模块(idea)或工程(eclipse)的 src 目录下。
 */
public class JDBCUtils {
    /**
     * 数据库连接池对象
     */
    private static DataSource ds;

    /*
     * 加载配置文件，并读取配置文件的内容，这一部分内容，使用静态代码块的方式
     * 完成(随着类的加载只执行一次)
     */
    static {
        try {
            Properties prop = new Properties();
            // 通过类加载器将配置文件变为字节输入流
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            assert is != null;
            prop.load(is);
            //通过工厂类的方法为静态变量 ds 赋值。
            ds = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象的方法
     * @return 数据库连接对象
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 释放资源，当程序没有用到查询语句的时候，使用这个方法。
     * @param st 需要释放的 Statement 资源
     * @param conn 需要释放的 Connection 资源
     */
    public static void closeResource(Statement st, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                // 归还数据库连接
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重载方法，用来释放资源。当使用到查询语句的时候使用该方法
     * @param rs 需要释放的 ResultSet 资源
     * @param st 需要释放的 Statement 资源
     * @param conn 需要释放的 Connection 资源
     */
    public static void closeResource(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 调用只释放 Statement, Connection 资源的重载方法
        closeResource(st, conn);
    }

    /**
     * 获取数据库连接池的方法
     * @return 数据库连接池
     */
    public static DataSource getDataSource() {
        return ds;
    }
}
