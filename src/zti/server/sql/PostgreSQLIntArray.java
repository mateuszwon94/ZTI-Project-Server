package zti.server.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import zti.server.data.Constants;

/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca tablice intow w bazie danych
 */
public class PostgreSQLIntArray implements java.sql.Array {
	/**
	 * Konstruktor
	 * @param intArray tablica na jakiej podstawie ma byc stworzona
	 */
	public PostgreSQLIntArray(Integer[] intArray) {
		this.intArray = intArray;
		this.stringValue = intArrayToPostgreSQLIntArray(this.intArray);
	}

	/**
	 * tekstowa reprezentacja tablicy
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return stringValue;
	}

	/**
	 * Przeksztalca na tablice w PostgreSQL
	 * @param intArray tablica do przeksztalcenia
	 * @return tekstowa reprezentacja tablicy
	 */
	public static String intArrayToPostgreSQLIntArray(Integer[] intArray) {
		final int arrayLength;
		if (intArray == null) {
			return Constants.NULL;
		} else if ((arrayLength = intArray.length) == 0) {
			return "{}";
		}
		
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (int si = 0; si < arrayLength; si++) {
			final Integer val = intArray[si];
			if (si > 0)
				sb.append(',');
			if (val == null) {
				sb.append(Constants.NULL);
			} else {
				for (int i = 0, l = val.toString().length(); i < l; i++) {
					final char ch = val.toString().charAt(i);
					sb.append(ch);
				}
			}
		}
		sb.append('}');

		return sb.toString();
	}

	/**
	 * @see java.sql.Array#getArray()
	 */
	@Override
	public Object getArray() throws SQLException {
		return intArray == null ? null : Arrays.copyOf(intArray, intArray.length);
	}

	/**
	 * @see java.sql.Array#getArray(java.util.Map)
	 */
	@Override
	public Object getArray(Map<String, Class<?>> map) throws SQLException {
		return getArray();
	}

	/**
	 * @see java.sql.Array#getArray(long, int)
	 */
	@Override
	public Object getArray(long index, int count) throws SQLException {
		return intArray == null ? null : Arrays.copyOfRange(intArray, (int) index, (int) index + count);
	}

	/**
	 * @see java.sql.Array#getArray(long, int, java.util.Map)
	 */
	@Override
	public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
		return getArray(index, count);
	}

	/**
	 * @see java.sql.Array#getBaseType()
	 */
	@Override
	public int getBaseType() throws SQLException {
		return java.sql.Types.INTEGER;
	}

	/**
	 * @see java.sql.Array#getBaseTypeName()
	 */
	@Override
	public String getBaseTypeName() throws SQLException {
		return "int";
	}

	/**
	 * @see java.sql.Array#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.sql.Array#getResultSet(java.util.Map)
	 */
	@Override
	public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.sql.Array#getResultSet(long, int)
	 */
	@Override
	public ResultSet getResultSet(long index, int count) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.sql.Array#getResultSet(long, int, java.util.Map)
	 */
	@Override
	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.sql.Array#free()
	 */
	@Override
	public void free() throws SQLException { }

	private final Integer[] intArray;
	private final String stringValue;
}