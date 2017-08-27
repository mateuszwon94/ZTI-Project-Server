package zti.server.data;

import java.time.LocalTime;

/**
 * @author Mateusz Winiarski
 * Klasa zawirajaca wszystkie stale uzywane w programie
 */
public final class Constants {
	public static final String STOPS 		= "stops";
	public static final String STOP 		= "stop";
	public static final String ID 			= "id";
	public static final String NAME 		= "name";
	public static final String NZ 			= "nz";
	public static final String LOC_X 		= "loc_x";
	public static final String LOC_Y 		= "loc_y";
	public static final String CONNS 		= "conns";
	public static final String CONN 		= "conn";
	public static final String TIMES 		= "times";
	public static final String TIME 		= "time";
	public static final String LINES 		= "lines";
	public static final String LINE 		= "line";
	public static final String NUMBER 		= "number";
	public static final String VARIANTS		= "variants";
	public static final String VARIANT		= "variant";
	public static final String ROUTE 		= "route";
	public static final String ROUTES 		= "routes";
	public static final String F_PEAK 		= "f_peak";
	public static final String F_NOT_PEAK 	= "f_not_peak";
	public static final String FIRST 		= "first";
	public static final String LAST 		= "last";
	public static final String SCHEDULES 	= "schedules";
	public static final String SCHEDULE 	= "schedule";
	public static final String CHANGE		= "change";
	public static final String LOGS			= "logs";
	public static final String LOG			= "log";
	public static final String FROM 		= "from";
	public static final String TO			= "to";
	public static final String NULL 		= "NULL";
	public static final String XSI			= "xmlns:xsi";
	public static final String XSD			= "xmlns:xsd";
	public static final String XSI_VAL		= "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSD_VAL		= "http://www.w3.org/2001/XMLSchema";
	
	public static final LocalTime MORNING_PEAK_START = LocalTime.of(7, 30);
	public static final LocalTime MORNING_PEAK_END = LocalTime.of(10, 00);
	public static final LocalTime AFTERNOON_PEAK_START = LocalTime.of(14, 30);
	public static final LocalTime AFTERNOON_PEAK_END = LocalTime.of(17, 30);
	
	/**
	 * @author Mateusz Winiarski
	 * Klasa zawierajaca zapytania do bazy danych
	 * Obecnie nie uzywane
	 */
	public final class Querys {
		public static final String OBJECT 				= "{$0}";
		public static final String GET_ALL_STOPS 		= "SELECT * FROM public.stops ORDER BY id";
		public static final String GET_ALL_LINES 		= "SELECT * FROM public.lines ORDER BY number";
		public static final String GET_SINGLE_LINE 		= "SELECT * FROM public.lines WHERE number = {$0};";
		public static final String GET_SINGLE_STOP 		= "SELECT * FROM public.stops WHERE id = {$0};";
		
		public static final String GET_SINGLE_LINE_JPA 	= "SELECT l FROM Line l WHERE l.number = {$0}";
		public static final String GET_SINGLE_STOP_JPA 	= "SELECT s FROM Stop s WHERE s.id = {$0}";
	}
}
