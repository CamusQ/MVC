package com.atguigu.mvcapp.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Jdbc 操作的工具类
 * @author LENOVO
 *
 */
public class JdbcUtils {
	
	/**
	 * 释放Connection连接
	 */
	public static void releaseConnection(Connection connection) {
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static DataSource dataSource = null;
	
	static {
		/*0数据源只能被创建一次*/
		dataSource = new ComboPooledDataSource("mvcapp");
	}
	
	/**
	 * 返回数据源的一个Connection对象
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}
