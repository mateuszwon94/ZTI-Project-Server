package zti.server.data.converter;

import zti.server.sql.PostgreSQLTextArray;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mateusz Winiarski
 * Konwerter z tablicy bazodanowej na liste stringow
 */
@Converter(autoApply = true)
public class TextListToArrayConveter implements AttributeConverter<List<String>, Object> {
	/**
	 * Funkcja konwertujaca na tablice bazodanowa
	 * @param attribute lista stringo
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public PostgreSQLTextArray convertToDatabaseColumn(List<String> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		String[] rst = new String[attribute.size()];
		return new PostgreSQLTextArray(attribute.toArray(rst));
	}

	/**
	 * Funkcja konwertujaca na liste intow
	 * @param dbData tablica bazodanowa
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public List<String> convertToEntityAttribute(Object dbData) {
		try {
			List<String> rst = new ArrayList<>();
			String[] elements = ((String[]) dbData);
			for (String element : elements) {
				rst.add(element);
			}
			return rst;
		} catch (Exception ex) {
			return null;
		}
	}
}