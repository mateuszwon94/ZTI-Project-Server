package zti.server.sql;

import java.util.*;

import org.w3c.dom.Element;

import zti.server.Stops;
import zti.server.data.*;
import zti.server.util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

public final class DataBaseConnection {
	
	private static final String url = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/xggfrvfc";
	private static final String username = "xggfrvfc";
	private static final String password = "q1gFyHQUPS0ZkVzS9nqmlshn0CzDNGgC";

	@Resource(name = "jdbc/postgresql")
	private static DataSource db;
	private static Connection conn = null;
	
	public static Statement createStatement() throws SQLException {
		if (conn == null) {
			conn = db.getConnection();
		}
		return conn.createStatement();
	}
	
	public static List<Stop> getStopList() {
		List<Stop> list = new ArrayList<Stop>();
		try {
				Statement stmt = createStatement();
				ResultSet rset = stmt.executeQuery(Constants.Querys.GET_ALL_STOPS);
				while (rset.next()) {
					Stop stop = new Stop();
					stop.setId(rset.getInt(Constants.ID));
					stop.setName(rset.getString(Constants.NAME));
					stop.setNz(rset.getBoolean(Constants.NZ));
					stop.setLocX(rset.getFloat(Constants.LOC_X));
					stop.setLocY(rset.getFloat(Constants.LOC_Y));
					stop.setConns((Integer[])(rset.getArray(Constants.CONNS).getArray()));
					stop.setTimes((Integer[])(rset.getArray(Constants.TIMES).getArray()));
					
					list.add(stop);
				}
				rset.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
		return list;
	}
}
