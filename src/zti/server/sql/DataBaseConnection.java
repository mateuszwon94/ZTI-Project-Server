package zti.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DataBaseConnection {
	
	private static final String url = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/xggfrvfc";
	private static final String username = "xggfrvfc";
	private static final String password = "q1gFyHQUPS0ZkVzS9nqmlshn0CzDNGgC";

	private static Connection conn = null;
	
	public static Statement createStatement() throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection(url, username, password);
		}
		return conn.createStatement();
	}
	
	public static final String GET_ALL_STOPS = "SELECT * FROM public.stops";
}
