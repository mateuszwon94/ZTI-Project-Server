package zti.server.sql;

import zti.server.data.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca tablice stringow w bazie danych
 */
public class PostgreSQLTextArray implements java.sql.Array {
	/**
	 * Konstruktor
	 * @param stringArray tablica na jakiej podstawie ma byc stworzona
	 */
	public PostgreSQLTextArray(String[] stringArray) {
		this.stringArray = stringArray;
		this.stringValue = stringArrayToPostgreSQLTextArray(this.stringArray);
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
	 * @param stringArray tablica do przeksztalcenia
	 * @return tekstowa reprezentacja tablicy
	 */
	public static String stringArrayToPostgreSQLTextArray(String[] stringArray) {
		final int arrayLength;
		if (stringArray == null) {
			return Constants.NULL;
		} else if ((arrayLength = stringArray.length) == 0) {
			return "{}";
		}
		// dlugosc stringa i czy trzeba wstawic cudzyslow
		int neededBufferLentgh = 2; // zawsze sa dwa znaki { i }
		boolean[] shouldQuoteArray = new boolean[stringArray.length];
		
		for (int si = 0; si < arrayLength; si++) {
			if (si > 0) // przecinek po pierwszym elemencie
				neededBufferLentgh++;

			boolean shouldQuote;
			final String s = stringArray[si];
			if (s == null) {
				neededBufferLentgh += 4;
				shouldQuote = false;
			} else {
				final int l = s.length();
				neededBufferLentgh += l;
				if (l == 0 || s.equalsIgnoreCase(Constants.NULL)) {
					shouldQuote = true;
				} else {
					shouldQuote = false;
					// szukanie znakow wymagajacych cudzyslowia
					for (int i = 0; i < l; i++) {
						final char ch = s.charAt(i);
						switch (ch) {
						case '"':
						case '\\':
							shouldQuote = true;
							// potrzeba dodatkowego znaku
							neededBufferLentgh++;
							break;
						case ',':
						case '\'':
						case '{':
						case '}':
							shouldQuote = true;
							break;
						default:
							if (Character.isWhitespace(ch)) {
								shouldQuote = true;
							}
							break;
						}
					}
				}
				// policzenie cudzyslowia
				if (shouldQuote)
					neededBufferLentgh += 2;
			}
			shouldQuoteArray[si] = shouldQuote;
		}

		// konstrukcja stringa
		final StringBuilder sb = new StringBuilder(neededBufferLentgh);
		sb.append('{');
		for (int si = 0; si < arrayLength; si++) {
			final String s = stringArray[si];
			if (si > 0)
				sb.append(',');
			if (s == null) {
				sb.append(Constants.NULL);
			} else {
				final boolean shouldQuote = shouldQuoteArray[si];
				if (shouldQuote)
					sb.append('"');
				for (int i = 0, l = s.length(); i < l; i++) {
					final char ch = s.charAt(i);
					if (ch == '"' || ch == '\\')
						sb.append('\\');
					sb.append(ch);
				}
				if (shouldQuote)
					sb.append('"');
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
		return stringArray == null ? null : Arrays.copyOf(stringArray, stringArray.length);
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
		return stringArray == null ? null : Arrays.copyOfRange(stringArray, (int) index, (int) index + count);
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

	private final String[] stringArray;
	private final String stringValue;
}