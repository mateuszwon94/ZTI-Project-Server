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
	
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public static ResultSet executeQuery(String query) throws SQLException, ClassNotFoundException {
		if (conn == null) {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
		}
		if (stmt == null) {
			stmt = conn.createStatement();
		}
		
		return stmt.executeQuery(query);
	}
	
	public static List<Stop> getStopList() {
		
		List<Stop> list = new ArrayList<Stop>();
		try {
				ResultSet rset = executeQuery(Constants.Querys.GET_ALL_STOPS);
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
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return list;
	}
	
	public static List<Line> getLineList() {
		List<Line> list = new ArrayList<Line>();
		try {
				ResultSet rset = executeQuery(Constants.Querys.GET_ALL_LINES);
				while (rset.next()) {
					Line line = new Line();
					line.setNumber(rset.getInt(Constants.NUMBER));
					try {
						line.setVariants((String[])(rset.getArray(Constants.VARIANTS).getArray()));
					} catch (NullPointerException ex) { }
					line.setRoute((Integer[])(rset.getArray(Constants.ROUTE).getArray()));
					line.setFPeak(rset.getInt(Constants.F_PEAK));
					line.setFNotPeak(rset.getInt(Constants.F_NOT_PEAK));
					line.setFirst(rset.getTime(Constants.FIRST));
					line.setLast(rset.getTime(Constants.LAST));
					
					list.add(line);
				}
				rset.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return list;
	}
}
