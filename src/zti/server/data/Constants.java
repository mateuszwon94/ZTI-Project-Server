package zti.server.data;

public final class Constants {
	public static final String STOPS 	= "stops";
	public static final String STOP 	= "stop";
	public static final String ID 		= "id";
	public static final String NAME 	= "name";
	public static final String NZ 		= "nz";
	public static final String LOC_X 	= "loc_x";
	public static final String LOC_Y 	= "loc_y";
	public static final String CONNS 	= "conns";
	public static final String CONN 	= "conn";
	public static final String TIMES 	= "times";
	public static final String ROUTE 	= "route";
	public static final String FROM 	= "from";
	public static final String TO		= "to";
	public static final String XSI		= "xmlns:xsi";
	public static final String XSD		= "xmlns:xsd";
	public static final String XSI_VAL	= "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSD_VAL	= "http://www.w3.org/2001/XMLSchema";
	
	public final class Querys {
		public static final String GET_ALL_STOPS = "SELECT * FROM public.stops ORDER BY id";
	}
}
