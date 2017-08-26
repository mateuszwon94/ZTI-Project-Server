package zti.server.data;

import java.time.LocalTime;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Util;

/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca rozklad jazdy
 */
public class Schedule {
	/**
	 * Konstruktor domyslny
	 */
	public Schedule() { }
	
	/**
	 * Konstruktor
	 * @param line linia dla jakiej wyznaczono rozklad
	 * @param stop przystanek dla jakiego wyznaczono rozklad
	 * @param times czasy odjazdow
	 */
	public Schedule(Line line, Stop stop, Map<String, List<LocalTime>> times) {
		this.line = line;
		this.stop = stop;
		this.times = times;
	}
	
	/**
	 * @return linia dla jakiej wyznaczono rozklad
	 */
	public Line getLine() { return line; }
	
	/**
	 * @param line nowa linia dla jakiej wyznaczono rozklad
	 */
	public void setLine(Line line) { this.line = line; }
	
	/**
	 * @return przystanek dla jakiego wyznaczono rozklad
	 */
	public Stop getStop() { return stop; }
	
	/**
	 * @param stop nowy przystanek dla jakiego wyznaczono rozklad
	 */
	public void setStop(Stop stop) { this.stop = stop; }
	
	/**
	 * @return czasy odjazdow
	 */
	public Map<String, List<LocalTime>> getTimes() { return times; }
	
	/**
	 * @param times nowe czasy odjazdow
	 */
	public void setTimes(Map<String, List<LocalTime>> times) { this.times = times; }
	
	/**
	 * Przeksztalca obiekt do postaci elementu XML
	 * @param doc Dokument w jakim generowany jest XML
	 * @return wygenerowany element XML
	 */
	public Element toXml(Document doc) {
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
		
		return scheduleElement;
	}
	
	private Line line;
	private Stop stop;
	private Map<String, List<LocalTime>> times;
}
