package com.atguigu.mvcapp.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Jdbc �����Ĺ�����
 * @author LENOVO
 *
 */
public class JdbcUtils {
	
	/**
	 * �ͷ�Connection����
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
		/*0����Դֻ�ܱ�����һ��*/
		dataSource = new ComboPooledDataSource("mvcapp");
	}
	
	/**
	 * ��������Դ��һ��Connection����
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}
