package zti.server.sql;

import java.util.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import zti.server.data.*;
 
/**
 * @author Mateusz Winiarski
 * Klasa realizujaca zapytania do bazy danych
 */
@ManagedBean(name = "DataBaseConnection")
@SessionScoped
public final class DataBaseConnection {
	/**
	 * Funkcja wykorzystuje mechanizm JPA
	 * @return wszystkie przystanki z bazy danych
	 */
	public static Map<Integer, Stop> getStopMap() {
		return baza.getStopMap();
	}

	/**
	 * Funkcja wykorzystuje mechanizm JPA
	 * @return wszystkie linie z bazy danych
	 */
	public static Map<Integer, Line> getLineMap() {
		return baza.getLineMap();
	}

	/**
	 * Funkcja wykorzystuje mechanizm JPA
	 * @param stopId ID szukanego przystanku
	 * @return przystanek o szukanym ID
	 */
	public static Stop getStop(Integer stopId) {
		return baza.getStop(stopId);
	}

	/**
	 * Funkcja wykorzystuje mechanizm JPA
	 * @param lineNumber numer szukanej linii
	 * @return linia o szukanym numerze
	 */
	public static Line getLine(Integer number) {
		return baza.getLine(number);
	}
	
	/**
	 * Funkcja wykorzystuje mechanizm JDBC
	 * Funkcja uzywana przed zaimplementowaniem JPA w projekcie pozostawiona w celach pogladowych
	 * @return wszystkie przystanki z bazy danych
	 */
	public static Map<Integer, Stop> getStopMapLegacy() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		try (Connection conn = DriverManager.getConnection(url, username, password);
			 Statement stmt = conn.createStatement();
			 ResultSet rset = stmt.executeQuery(Constants.Querys.GET_ALL_STOPS)) {
			Map<Integer, Stop> stops = new HashMap<Integer, Stop>();

			while (rset.next()) {
				Stop stop = new Stop();
				stop.setId(rset.getInt(Constants.ID));
				stop.setName(rset.getString(Constants.NAME));
				stop.setNz(rset.getBoolean(Constants.NZ));
				stop.setLocX(rset.getFloat(Constants.LOC_X));
				stop.setLocY(rset.getFloat(Constants.LOC_Y));
				stop.setConns(
						new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.CONNS).getArray())));
				stop.setTimes(
						new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.TIMES).getArray())));

				stops.put(stop.getId(), stop);
			}

			return stops;
		}
	}

	/**
	 * Funkcja wykorzystuje mechanizm JDBC
	 * Funkcja uzywana przed zaimplementowaniem JPA w projekcie pozostawiona w celach pogladowych
	 * @return wszystkie linie z bazy danych
	 */
	public static Map<Integer, Line> getLineMapLegacy() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		try (Connection conn = DriverManager.getConnection(url, username, password);
			 Statement stmt = conn.createStatement();
			 ResultSet rset = stmt.executeQuery(Constants.Querys.GET_ALL_LINES)) {
			Map<Integer, Line> lines = new HashMap<Integer, Line>();

			while (rset.next()) {
				Line line = new Line();
				line.setNumber(rset.getInt(Constants.NUMBER));
				try {
					line.setVariants(Arrays.asList((String[]) (rset.getArray(Constants.VARIANTS).getArray())));
				} catch (NullPointerException ex) { }
				line.setRoute(new ArrayList<Integer>(Arrays.asList((Integer[]) (rset.getArray(Constants.ROUTE).getArray()))));
				line.setFPeak(rset.getInt(Constants.F_PEAK));
				line.setFNotPeak(rset.getInt(Constants.F_NOT_PEAK));
				line.setFirst(rset.getTime(Constants.FIRST));
				line.setLast(rset.getTime(Constants.LAST));

				lines.put(line.getNumber(), line);
			}

			return lines;
		}
	}

	/**
	 * Funkcja wykorzystuje mechanizm JDBC
	 * Funkcja uzywana przed zaimplementowaniem JPA w projekcie pozostawiona w celach pogladowych
	 * @param stopId ID szukanego przystanku
	 * @return przystanek o szukanym ID
	 */
	public static Stop getStopLegacy(Integer stopId) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		try (Connection conn = DriverManager.getConnection(url, username, password);
			 Statement stmt = conn.createStatement();
			 ResultSet rset = stmt.executeQuery(Constants.Querys.GET_SINGLE_STOP.replace(Constants.Querys.OBJECT, stopId.toString()))) {
			Stop stop = new Stop();

			rset.next();
			stop.setId(rset.getInt(Constants.ID));
			stop.setName(rset.getString(Constants.NAME));
			stop.setNz(rset.getBoolean(Constants.NZ));
			stop.setLocX(rset.getFloat(Constants.LOC_X));
			stop.setLocY(rset.getFloat(Constants.LOC_Y));
			stop.setConns(new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.CONNS).getArray())));
			stop.setTimes(new ArrayList<Integer>(Arrays.asList((Integer[]) rset.getArray(Constants.TIMES).getArray())));

			return stop;
		}
	}

	/**
	 * Funkcja wykorzystuje mechanizm JDBC
	 * Funkcja uzywana przed zaimplementowaniem JPA w projekcie pozostawiona w celach pogladowych
	 * @param lineNumber numer szukanej linii
	 * @return linia o szukanym numerze
	 */
	public static Line getLineLegacy(Integer lineNumber) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		try (Connection conn = DriverManager.getConnection(url, username, password);
			 Statement stmt = conn.createStatement();
			 ResultSet rset = stmt.executeQuery(Constants.Querys.GET_SINGLE_LINE.replace(Constants.Querys.OBJECT, lineNumber.toString()))) {
			Line line = new Line();

			rset.next();
			line.setNumber(rset.getInt(Constants.NUMBER));
			try {
				line.setVariants(Arrays.asList((String[]) (rset.getArray(Constants.VARIANTS).getArray())));
			} catch (NullPointerException ex) { }
			line.setRoute(new ArrayList<Integer>(Arrays.asList((Integer[]) (rset.getArray(Constants.ROUTE).getArray()))));
			line.setFPeak(rset.getInt(Constants.F_PEAK));
			line.setFNotPeak(rset.getInt(Constants.F_NOT_PEAK));
			line.setFirst(rset.getTime(Constants.FIRST));
			line.setLast(rset.getTime(Constants.LAST));

			return line;
		}
	}

	private static final String url = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/xggfrvfc";
	private static final String username = "xggfrvfc";
	private static final String password = "q1gFyHQUPS0ZkVzS9nqmlshn0CzDNGgC";
	
	private static final ConnJPA baza = new ConnJPA();
}
