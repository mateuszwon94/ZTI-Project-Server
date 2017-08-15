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

	public static Map<Integer, Stop> getStopMap() {
		Map<Integer, Stop> stops = new HashMap<Integer, Stop>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			rset = stmt.executeQuery(Constants.Querys.GET_ALL_STOPS);
			while (rset.next()) {
				Stop stop = new Stop();
				stop.setId(rset.getInt(Constants.ID));
				stop.setName(rset.getString(Constants.NAME));
				stop.setNz(rset.getBoolean(Constants.NZ));
				stop.setLocX(rset.getFloat(Constants.LOC_X));
				stop.setLocY(rset.getFloat(Constants.LOC_Y));
				stop.setConns(new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.CONNS).getArray())));
				stop.setTimes(new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.TIMES).getArray())));

				stops.put(stop.getId(), stop);
			}
			rset.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (rset != null)
					rset.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return stops;
	}

	public static Map<Integer, Line> getLineMap() {
		Map<Integer, Line> lines = new HashMap<Integer, Line>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			rset = stmt.executeQuery(Constants.Querys.GET_ALL_LINES);
			while (rset.next()) {
				Line line = new Line();
				line.setNumber(rset.getInt(Constants.NUMBER));
				try {
					line.setVariants(Arrays.asList((String[]) (rset.getArray(Constants.VARIANTS).getArray())));
				} catch (NullPointerException ex) {
				}
				line.setRoute(new ArrayList<Integer>(Arrays.asList((Integer[]) (rset.getArray(Constants.ROUTE).getArray()))));
				line.setFPeak(rset.getInt(Constants.F_PEAK));
				line.setFNotPeak(rset.getInt(Constants.F_NOT_PEAK));
				line.setFirst(rset.getTime(Constants.FIRST));
				line.setLast(rset.getTime(Constants.LAST));

				lines.put(line.getNumber(), line);
			}
			rset.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (rset != null)
					rset.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lines;
	}
}
