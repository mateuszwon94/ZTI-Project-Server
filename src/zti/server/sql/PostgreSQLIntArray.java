package zti.server.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import zti.server.data.Constants;

public class PostgreSQLIntArray implements java.sql.Array {
	public PostgreSQLIntArray(Integer[] stringArray) {
		this.intArray = stringArray;
		this.stringValue = stringArrayToPostgreSQLTextArray(this.intArray);
	}

	@Override
	public String toString() {
		return stringValue;
	}

	/**
	 * This static method can be used to convert an string array to string
	 * representation of PostgreSQL text array.
	 * 
	 * @param a
	 *            source String array
	 * @return string representation of a given text array
	 */
	public static String stringArrayToPostgreSQLTextArray(Integer[] stringArray) {
		final int arrayLength;
		if (stringArray == null) {
			return Constants.NULL;
		} else if ((arrayLength = stringArray.length) == 0) {
			return "{}";
		}
		// count the string length and if need to quote
		int neededBufferLentgh = 2; // count the beginning '{' and the ending '}' brackets
		
		// construct the String
		final StringBuilder sb = new StringBuilder(neededBufferLentgh);
		sb.append('{');
		for (int si = 0; si < arrayLength; si++) {
			final Integer val = stringArray[si];
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
		assert sb.length() == neededBufferLentgh;
		return sb.toString();
	}

	@Override
	public Object getArray() throws SQLException {
		return intArray == null ? null : Arrays.copyOf(intArray, intArray.length);
	}

	@Override
	public Object getArray(Map<String, Class<?>> map) throws SQLException {
		return getArray();
	}

	@Override
	public Object getArray(long index, int count) throws SQLException {
		return intArray == null ? null : Arrays.copyOfRange(intArray, (int) index, (int) index + count);
	}

	@Override
	public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
		return getArray(index, count);
	}

	@Override
	public int getBaseType() throws SQLException {
		return java.sql.Types.INTEGER;
	}

	@Override
	public String getBaseTypeName() throws SQLException {
		return "int";
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet(long index, int count) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void free() throws SQLException { }

	private final Integer[] intArray;
	private final String stringValue;
}