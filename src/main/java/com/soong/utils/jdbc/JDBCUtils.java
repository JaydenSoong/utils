package com.soong.utils.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作 JDBC 的工具类，提供注册驱动、获取数据库连接、释放资源的方法。
 * 使用该类需要提供一个 jdbc.properties 的配置文件，该文件包括数据库连接的 url、username、password
 * 以及数据库驱动的完整类名。将该文件放大模块(idea)或工程(eclipse)的 src 目录下。
 */
public class JDBCUtils {
    /**
     * 所使用数据库的 url
     */
    private static String url;
    /**
     * 连接数据库的用户名
     */
    private static String username;
    /**
     * 连接数据库的密码
     */
    private static String password;
    /*
     * 加载配置文件，并读取配置文件的内容，这一部分内容，使用静态代码块的方式
     * 完成(随着类的加载只执行一次)
     */
    static {
        try {
            Properties prop = new Properties();
            // 通过类加载器将配置文件变为字节输入流
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            assert is != null;
            prop.load(is);
            // 读取配置文件中的内容
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            String driver = prop.getProperty("driver");
            //  注册驱动，mysql 5 及以上的版本，这一步是可以省略的
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象的方法
     * @return 数据库连接对象
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
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
}
