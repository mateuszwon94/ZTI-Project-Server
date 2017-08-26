package zti.server.data.converter;

import zti.server.sql.PostgreSQLTextArray;
import org.postgresql.jdbc4.Jdbc4Array;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;

/**
 * Created by study on 11/15/14.
 */
@Converter(autoApply = true)
public class TextListToArrayConveter implements AttributeConverter<List<String>, Object> {
	@Override
	public PostgreSQLTextArray convertToDatabaseColumn(List<String> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		String[] rst = new String[attribute.size()];
		return new PostgreSQLTextArray(attribute.toArray(rst));
	}

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