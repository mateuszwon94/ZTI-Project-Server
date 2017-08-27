package zti.server.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Time;
import java.time.LocalTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import zti.server.data.*;
import zti.server.util.Pair;
import zti.server.util.Util;

public class XmlGenerationTest {

	/**
	 * Sprawdza czy poprawnie generowany jest xml dla linii bez wyszczegolnionych wariantow
	 */
	@Test
	public void testLineWithoutVariantsToXml() throws ParserConfigurationException {
		Integer lineNumber = new Integer(1);
		
		List<String> variants = null;
		
		List<Integer> route = new ArrayList<>(5);
		route.add(1);
		route.add(2);
		route.add(3);
		route.add(4);
		route.add(5);
		
		Integer f_peak = new Integer(10);
		Integer f_not_peak = new Integer(20);
		
		Time first = Time.valueOf("6:30:00");
		Time last = Time.valueOf("23:0:0");

		Line line = new Line(lineNumber, variants, route, f_peak, f_not_peak, first, last);

		Document lineDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		lineDoc.appendChild(line.toXml(lineDoc));
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element lineElement = doc.createElement(Constants.LINE);
		lineElement.setAttribute(Constants.NUMBER, lineNumber.toString());
		Element routeElement = doc.createElement(Constants.ROUTE);
		for (Integer stop : route) {
			routeElement.appendChild(Util.createElement(doc, Constants.STOP, stop.toString()));
		}
		lineElement.appendChild(routeElement);
		
		lineElement.appendChild(Util.createElement(doc, Constants.F_PEAK, f_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.F_NOT_PEAK, f_not_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.FIRST, first.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.LAST, last.toString()));
		
		doc.appendChild(lineElement);
		
		assertEquals(doc.toString(), lineDoc.toString());
	}

	/**
	 * Sprawdza czy poprawnie generowany jest xml dla linii z wyszczegolnionionymi wariantami
	 */
	@Test
	public void testLineWithVariantsToXml() throws ParserConfigurationException {
		Integer lineNumber = new Integer(1);
		
		List<String> variants = new ArrayList<>(2);
		variants.add("1");
		variants.add("2");
		
		List<Integer> route = new ArrayList<>(5);
		route.add(1);
		route.add(2);
		route.add(3);
		route.add(4);
		route.add(5);
		
		Integer f_peak = new Integer(10);
		Integer f_not_peak = new Integer(20);
		
		Time first = Time.valueOf("6:30:00");
		Time last = Time.valueOf("23:0:0");

		Line line = new Line(lineNumber, variants, route, f_peak, f_not_peak, first, last);

		Document lineDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		lineDoc.appendChild(line.toXml(lineDoc));
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element lineElement = doc.createElement(Constants.LINE);
		lineElement.setAttribute(Constants.NUMBER, lineNumber.toString());
		Element variantsElement = doc.createElement(Constants.VARIANTS);
		for (String variant : variants)
			variantsElement.appendChild(Util.createElement(doc, Constants.VARIANT, variant));
		lineElement.appendChild(variantsElement);
		Element routeElement = doc.createElement(Constants.ROUTE);
		for (Integer stop : route) {
			routeElement.appendChild(Util.createElement(doc, Constants.STOP, stop.toString()));
		}
		lineElement.appendChild(routeElement);
		
		lineElement.appendChild(Util.createElement(doc, Constants.F_PEAK, f_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.F_NOT_PEAK, f_not_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.FIRST, first.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.LAST, last.toString()));
		
		doc.appendChild(lineElement);
		
		assertEquals(doc.toString(), lineDoc.toString());
	}
	
	/**
	 * Sprawdza czy poprawnie generowany jest xml dla przystanku
	 */
	@Test
	public void testStopToXml() throws ParserConfigurationException {
		Integer id = new Integer (5);
		String name = "Nazwa";
		Boolean nz = new Boolean(false);
		Float loc_x = new Float(5.4);
		Float loc_y = new Float(4.5);
		
		List<Integer> conns = new ArrayList<>(4);
		conns.add(1);
		conns.add(2);
		conns.add(3);
		conns.add(4);
		
		List<Integer> times = new ArrayList<>(4);
		times.add(2);
		times.add(3);
		times.add(2);
		times.add(4);

		Stop stop = new Stop(id, name, nz, loc_x, loc_y, conns, times);

		Document stopDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		stopDoc.appendChild(stop.toXml(stopDoc));
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element stopElement = doc.createElement(Constants.STOP);
		
		stopElement.setAttribute(Constants.ID, id.toString());
		stopElement.appendChild(Util.createElement(doc, Constants.NAME, name));
		stopElement.appendChild(Util.createElement(doc, Constants.NZ, nz.toString()));
		stopElement.appendChild(Util.createElement(doc, Constants.LOC_X, loc_x.toString()));
		stopElement.appendChild(Util.createElement(doc, Constants.LOC_Y, loc_y.toString()));
		
		Element connsElement = doc.createElement(Constants.CONNS);
		for (Integer conn : conns) {
			connsElement.appendChild(Util.createElement(doc, Constants.CONN, conn.toString()));
		}
		stopElement.appendChild(connsElement);
		
		doc.appendChild(stopElement);
		
		assertEquals(doc.toString(), stopDoc.toString());
	}
	
	/**
	 * Sprawdza czy poprawnie generowany jest xml dla wyznaczonej trasy
	 */
	@Test
	public void testRouteToXml() throws ParserConfigurationException {
		List<Stop> path = new ArrayList<>(3);
		path.add(new Stop(1, "1", false, 1.0f, 1.0f, null, null));
		path.add(new Stop(2, "2", false, 2.0f, 2.0f, null, null));
		path.add(new Stop(3, "3", false, 3.0f, 3.0f, null, null));
		
		Line line = new Line(5, null, null, null, null, null, null);
		
		Map<Stop, Pair<Line, LocalTime>> val = new HashMap<>(3);
		int min = 30;
		for (Stop stop : path) {
			val.put(stop, new Pair<>(line, LocalTime.of(5, min++)));
		}

		Route route = new Route(val);

		Document routeDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		routeDoc.appendChild(route.toXml(routeDoc, path));
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element routeElement = doc.createElement(Constants.ROUTE);

		for (Stop stop : path) {
			Element lineElement = doc.createElement(Constants.STOP);
			lineElement.setAttribute(Constants.ID, stop.getId().toString());
			lineElement.appendChild(Util.createElement(doc, Constants.LINE, val.get(stop).getKey().getNumber().toString()));
			lineElement.appendChild(Util.createElement(doc, Constants.TIME, val.get(stop).getValue().toString()));

			routeElement.appendChild(lineElement);
		}
		
		doc.appendChild(routeElement);
		
		assertEquals(doc.toString(), routeDoc.toString());
	}
	
	/**
	 * Sprawdza czy poprawnie generowany jest xml dla rozkladu jazdy
	 */
	@Test
	public void testScheduleToXml() throws ParserConfigurationException {
		Line line = new Line(5, null, null, null, null, null, null);
		Stop stop = new Stop(1, "1", false, 1.0f, 1.0f, null, null);
		
		List<LocalTime> localTimes = new ArrayList<>(3);
		localTimes.add(LocalTime.of(6, 20));
		localTimes.add(LocalTime.of(6, 22));
		localTimes.add(LocalTime.of(6, 25));
		
		Map<String, List<LocalTime>> times = new HashMap<>(2);
		times.put("1", localTimes);
		times.put("2", localTimes);

		Schedule schedule = new Schedule(line, stop, times);

		Document scheduleDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		scheduleDoc.appendChild(schedule.toXml(scheduleDoc));
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element scheduleElement = doc.createElement(Constants.SCHEDULE);
		scheduleElement.setAttribute(Constants.LINE, line.getNumber().toString());
		scheduleElement.setAttribute(Constants.STOP, stop.getId().toString());
		
		for (Map.Entry<String, List<LocalTime>> timesEntry : times.entrySet()) {
			Element timesElement = doc.createElement(Constants.TIMES);
			timesElement.setAttribute(Constants.VARIANT, timesEntry.getKey());
			
			for (LocalTime time : timesEntry.getValue()) {
				timesElement.appendChild(Util.createElement(doc, Constants.TIME, time.toString()));
			}
			
			scheduleElement.appendChild(timesElement);
		}
		
		doc.appendChild(scheduleElement);
		
		assertEquals(doc.toString(), scheduleDoc.toString());
	}
}
