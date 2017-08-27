package zti.server.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import zti.server.data.Line;
import zti.server.data.Route;
import zti.server.data.Stop;
import zti.server.data.exception.PathNotFoundException;
import zti.server.util.*;

public class UtilTest {
	@Before
	public void setUp() throws Exception {
		for (int i = 0; i < 10; ++i)
			allStops.put(i, new Stop(i, Integer.toString(i), false, new Float(0), new Float(0), new ArrayList<>(),
					new ArrayList<>()));

		allStops.get(1).setLocX(new Float(1.5));
		allStops.get(1).setLocY(new Float(5));
		allStops.get(1).setConns(Arrays.asList(2, 5, 6));
		allStops.get(1).setTimes(Arrays.asList(2, 3, 2));

		allStops.get(2).setLocX(new Float(3));
		allStops.get(2).setLocY(new Float(4));
		allStops.get(2).setConns(Arrays.asList(1, 3, 6, 7));
		allStops.get(2).setTimes(Arrays.asList(2, 3, 3, 3));

		allStops.get(3).setLocX(new Float(2.5));
		allStops.get(3).setLocY(new Float(2));
		allStops.get(3).setConns(Arrays.asList(2, 4, 8));
		allStops.get(3).setTimes(Arrays.asList(3, 2, 3));

		allStops.get(4).setLocX(new Float(1));
		allStops.get(4).setLocY(new Float(2));
		allStops.get(4).setConns(Arrays.asList(3, 5));
		allStops.get(4).setTimes(Arrays.asList(2, 3));

		allStops.get(5).setLocX(new Float(0.5));
		allStops.get(5).setLocY(new Float(3.5));
		allStops.get(5).setConns(Arrays.asList(4, 1));
		allStops.get(5).setTimes(Arrays.asList(4, 2));

		allStops.get(6).setLocX(new Float(3));
		allStops.get(6).setLocY(new Float(6.5));
		allStops.get(6).setConns(Arrays.asList(1, 2));
		allStops.get(6).setTimes(Arrays.asList(2, 3));

		allStops.get(7).setLocX(new Float(5));
		allStops.get(7).setLocY(new Float(4));
		allStops.get(7).setConns(Arrays.asList(2, 8));
		allStops.get(7).setTimes(Arrays.asList(3, 4));

		allStops.get(8).setLocX(new Float(4.5));
		allStops.get(8).setLocY(new Float(2));
		allStops.get(8).setConns(Arrays.asList(7, 3, 9));
		allStops.get(8).setTimes(Arrays.asList(4, 3, 5));

		allStops.get(9).setLocX(new Float(4.5));
		allStops.get(9).setLocY(new Float(0));
		allStops.get(9).setConns(Arrays.asList(8));
		allStops.get(9).setTimes(Arrays.asList(5));

		for (int i = 1; i <= 5; ++i)
			allLines.put(i, new Line(i, null, null, new Integer(10), new Integer(i % 2 == 0 ? 10 : 20),
					Time.valueOf("05:30:00"), Time.valueOf("22:30:00")));

		allLines.get(1).setRoute(Arrays.asList(9, 8, 7, 2, 6, 1, 5));
		allLines.get(2).setRoute(Arrays.asList(9, 8, 3, 4, 5));
		allLines.get(3).setRoute(Arrays.asList(8, 3, 4, 5, 1, 6));
		allLines.get(4).setRoute(Arrays.asList(1, 2, 7, 8, 9));
		allLines.get(5).setRoute(Arrays.asList(6, 2, 3, 4));
	}

	@Test
	public void testGeneratePath() throws ClassNotFoundException, PathNotFoundException, SQLException {
		try {
			Util.generatePath(null, 1, allStops);
			fail("Generate path should throw nullRef becouse 'from' is null");
		} catch (NullPointerException ex) { }
		
		try {
			Util.generatePath(1, null, allStops);
			fail("Generate path should throw nullRef becouse 'to' is null");
		} catch (NullPointerException ex) { }
		
		try {
			Util.generatePath(1, 10, allStops);
			fail("Generate path should throw nullRef becouse 'from' is not preset on map");
		} catch (NullPointerException ex) { }
		
		try {
			Util.generatePath(10, 1, allStops);
			fail("Generate path should throw nullRef becouse 'to' is not preset on map");
		} catch (NullPointerException ex) { }
		
		Integer fromId, toId;
		
		fromId = 1;
		toId = 2;
		assertEquals("Generate path should be stops: {1, 2}.", Util.generatePath(fromId, toId, allStops), Arrays.asList(allStops.get(fromId), allStops.get(toId)));
		
		toId = 9;
		assertEquals("Generate path should be stops: {1, 2, 3, 8, 9}.", Util.generatePath(fromId, toId, allStops), Arrays.asList(allStops.get(fromId), allStops.get(2), allStops.get(3), allStops.get(8), allStops.get(toId)));
		
		fromId = 5;
		assertEquals("Generate path should be stops: {5, 4, 3, 8, 9}.", Util.generatePath(fromId, toId, allStops), Arrays.asList(allStops.get(fromId), allStops.get(4), allStops.get(3), allStops.get(8), allStops.get(toId)));
		
		toId = 7;
		assertEquals("Generate path should be stops: {5, 1, 2, 7}.", Util.generatePath(fromId, toId, allStops), Arrays.asList(allStops.get(fromId), allStops.get(1), allStops.get(2), allStops.get(toId)));
		
		fromId = 4;
		toId = 6;
		assertEquals("Generate path should be stops: {4, 5, 1, 6}.", Util.generatePath(fromId, toId, allStops), Arrays.asList(allStops.get(fromId), allStops.get(5), allStops.get(1), allStops.get(toId)));
		
		try {
			Util.generatePath(1, 0, allStops);
			fail("Generate path should throw PathNotFoundException becouse 'to' is not connected map");
		} catch (PathNotFoundException ex) { }
	}

	private Map<Integer, Stop> allStops = new HashMap<>();
	private Map<Integer, Line> allLines = new HashMap<>();
}
