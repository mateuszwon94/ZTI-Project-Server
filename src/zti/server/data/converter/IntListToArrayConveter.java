package zti.server.data.converter;

import zti.server.sql.PostgreSQLIntArray;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mateusz Winiarski
 * Konwerter z tablicy bazodanowej na liste intow
 */
@Converter(autoApply = true)
public class IntListToArrayConveter implements AttributeConverter<List<Integer>, Object> {
	/**
	 * Funkcja konwertujaca na tablice bazodanowa
	 * @param attribute lista intow
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public PostgreSQLIntArray convertToDatabaseColumn(List<Integer> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		Integer[] rst = new Integer[attribute.size()];
		return new PostgreSQLIntArray(attribute.toArray(rst));
	}

	/**
	 * Funkcja konwertujaca na liste intow
	 * @param dbData tablica bazodanowa
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public List<Integer> convertToEntityAttribute(Object dbData) {
		try {
			List<Integer> rst = new ArrayList<>();
			Integer[] elements = ((Integer[]) dbData);
			for (Integer element : elements) {
				rst.add(element);
			}
			return rst;
		} catch (Exception ex) {
			return null;
		}
	}
}