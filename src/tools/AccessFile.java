package tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AccessFile {
	
	//get connection
	public Connection getConnection(String file){
		Connection conn = null;
		try {
			String path = new File(file).getAbsolutePath();
			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + path;
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * @return
	 */
	public int executeUpdateAccess(Connection conn, String sql) {
		int result = 0;
		try {
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @return
	 */
	public int reExecuteUpdateAccess(Connection conn, String sql, String[] value) {
		int result = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(int i=1;i<=value.length;i++){
				stmt.setString(i, value[i-1]);
			}
			result = stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * close connection
	 * @param conn
	 * @return
	 */
	public boolean closeConn (Connection conn){
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
