package zti.server.data.converter;

import zti.server.sql.PostgreSQLIntArray;
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
public class IntListToArrayConveter implements AttributeConverter<List<Integer>, Object> {
	@Override
	public PostgreSQLIntArray convertToDatabaseColumn(List<Integer> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		Integer[] rst = new Integer[attribute.size()];
		return new PostgreSQLIntArray(attribute.toArray(rst));
	}

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