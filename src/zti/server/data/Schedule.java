package zti.server.data;

import java.time.LocalTime;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Util;

public class Schedule {
	public Schedule() { }
	
	public Schedule(Line line, Stop stop, Map<String, List<LocalTime>> times) {
		this.line = line;
		this.stop = stop;
		this.times = times;
	}
	
	public Line getLine() { return line; }
	public void setLine(Line line) { this.line = line; }
	
	public Stop getStop() { return stop; }
	public void setStop(Stop stop) { this.stop = stop; }
	
	public Map<String, List<LocalTime>> getTimes() { return times; }
	public void setTimes(Map<String, List<LocalTime>> times) { this.times = times; }
	
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
