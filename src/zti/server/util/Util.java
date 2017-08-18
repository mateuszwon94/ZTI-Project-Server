package zti.server.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.util.Pair;
import zti.server.data.*;
import zti.server.data.exception.PathNotFoundException;
import zti.server.sql.DataBaseConnection;

public final class Util {
	public static Element createElement(Document doc, String name, String content) {
		Element elem = doc.createElement(name);
		elem.appendChild(doc.createTextNode(content));
		return elem;
	}

	public static void printException(Exception e, PrintWriter out) {
		out.print(e);
		out.print("<br /><br />");
		for (StackTraceElement ste : e.getStackTrace()) {
			out.print(ste.toString());
			out.print("<br />");
		}
	}

	public static void writeXmlToPrintWriter(Document doc, PrintWriter out) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

	public static List<Stop> generatePath(Integer fromID, Integer toID)
			throws PathNotFoundException, ClassNotFoundException, SQLException {
		if (fromID == null)
			throw new NullPointerException("fromID");
		else if (toID == null)
			throw new NullPointerException("toID");

		Map<Integer, Stop> stopMap = DataBaseConnection.getStopMap();
		Stop from = stopMap.getOrDefault(fromID, null);
		Stop to = stopMap.getOrDefault(toID, null);
		if (from == null)
			throw new NullPointerException("from");
		else if (to == null)
			throw new NullPointerException("to");

		if (stopMap.get(from.getId()).getConns().contains(to.getId()))
			return Arrays.asList(from, to);

		List<Node> openList = new ArrayList<Node>(Arrays.asList(new Node(from)));
		List<Node> closeList = new ArrayList<Node>(stopMap.size());

		Node endNode = null;
		while (openList.size() != 0) {
			Node current = openList.get(0);
			openList.remove(0);
			closeList.add(current);

			if (current.stop == to) {
				endNode = current;
			} else if (endNode != null && minValueCostFromStartFromOpenList(openList) < endNode.costFromStart) {
				return reconstructPath(endNode);
			}

			for (int i = 0; i < current.stop.getConns().size(); ++i) {
				Node newNode = new Node(stopMap.get(current.stop.getConns().get(i)), current);
				newNode.costFromStart = current.costFromStart + distance(current.stop, newNode.stop);
				newNode.costToEnd = distance(newNode.stop, to);

				if (closeList.contains(newNode)) {
					Node oldNode = closeList.get(closeList.indexOf(newNode));
					if (newNode.costFromStart < oldNode.costFromStart) {
						closeList.remove(oldNode);
						openList.add(newNode);
					}
				} else if (openList.contains(newNode)) {
					Node oldNode = openList.get(openList.indexOf(newNode));
					if (newNode.costFromStart < oldNode.costFromStart) {
						oldNode.costFromStart = newNode.costFromStart;
						oldNode.parent = newNode.parent;
					}
				} else {
					openList.add(newNode);
				}
			}

			Collections.sort(openList);
		}

		throw new PathNotFoundException();
	}
	
	public static Schedule getSchedule(Integer lineNum, Integer stopId) throws ClassNotFoundException, SQLException {
		Line line = DataBaseConnection.getLine(lineNum);
		Map<Integer, Stop> allStops = DataBaseConnection.getStopMap();
		Stop stop = allStops.get(stopId);
		
		Schedule schedule = new Schedule();
		schedule.setLine(line);
		schedule.setStop(stop);
		Map<String, List<LocalTime>> times = new HashMap<String, List<LocalTime>>();
		if (line.getVariants() == null) {
			times.put("0", generateSchedule(line, stop, true, allStops));
			times.put("1", generateSchedule(line, stop, false, allStops));
		} else {
			times.put(line.getVariants().get(0), generateSchedule(line, stop, true, allStops));
			times.put(line.getVariants().get(1), generateSchedule(line, stop, false, allStops));
		}
		schedule.setTimes(times);
		
		return schedule;
	}
	
	public static List<Line> getAllLineOnStop(Stop stop, Map<Integer, Line> allLines) {
		List<Line> lines = new ArrayList<Line>();
		
		for (Map.Entry<Integer, Line> lineEntry : allLines.entrySet()) {
			if (lineEntry.getValue().getRoute().contains(stop.getId())) {
				lines.add(lineEntry.getValue());
			}
		}
		
		return lines;
	}
	
	public static Route generateRoute(List<Stop> path, LocalTime startTime)
			throws ClassNotFoundException, SQLException {
		Map<Integer, Line> allLines = DataBaseConnection.getLineMap();
		Map<Integer, Stop> allStops = DataBaseConnection.getStopMap();

		Map<Stop, Pair<Line, LocalTime>> route = new HashMap<Stop, Pair<Line, LocalTime>>();
		Line currentLine = null;
		LocalTime currentTime = LocalTime.of(startTime.getHour(), startTime.getMinute());
		int currentScheduleIndex = -1;
		boolean currentDirection = true;
		for (int i = 0; i < path.size(); ++i) {
			Stop currentStop = path.get(i);

			List<Line> linesOnStop = getAllLineOnStop(currentStop, allLines);
			if (currentLine == null || (i < path.size() -1 && !currentLine.getRoute().contains(path.get(i + 1)))) { //jesli poczatek lub linia 
				Map<Line, Boolean> interestingLines = new HashMap<Line, Boolean>();
				for (Line line : linesOnStop) {
					if (line.getRoute().contains(path.get(i + 1).getId())) {
						interestingLines.put(line, getDirection(line, currentStop, path.get(i + 1)));
					}
				}

				Map<Line, Pair<LocalTime, Integer>> lineFirstDeparture = new HashMap<Line, Pair<LocalTime, Integer>>();
				for (Map.Entry<Line, Boolean> entry : interestingLines.entrySet()) {
					List<LocalTime> lineSchedule = generateSchedule(entry.getKey(), currentStop, entry.getValue(), allStops);

					for (int j = 0; j < lineSchedule.size(); ++j) {
						if (lineSchedule.get(j).isAfter(currentTime)) {
							lineFirstDeparture.put(entry.getKey(),
									new Pair<LocalTime, Integer>(lineSchedule.get(j), j));
							break;
						}
					}
				}

				LocalTime minTime = LocalTime.MAX;
				for (Map.Entry<Line, Pair<LocalTime, Integer>> entry : lineFirstDeparture.entrySet()) {
					if (entry.getValue().getKey().isBefore(minTime)) {
						minTime = entry.getValue().getKey();
						currentLine = entry.getKey();
						currentScheduleIndex = entry.getValue().getValue();
						currentDirection = getDirection(currentLine, currentStop, path.get(i + 1));
						currentTime = entry.getValue().getKey();
					}
				}

				route.put(currentStop, new Pair<Line, LocalTime>(currentLine, currentTime));
				
			} else {
				List<LocalTime> lineSchedule = generateSchedule(currentLine, currentStop, currentDirection, allStops);
				currentTime = lineSchedule.get(currentScheduleIndex);
				route.put(currentStop, new Pair<Line, LocalTime>(currentLine, currentTime));
			}
		}

		return new Route(route);
	}

	private static List<Stop> reconstructPath(Node node) {
		List<Stop> route = new ArrayList<Stop>();

		while (true) {
			route.add(0, node.stop);
			if (node.parent == null)
				break;
			node = node.parent;
		}

		return route;
	}

	private static float distance(Stop from, Stop to) {
		return (float) Math.sqrt(((from.getLocX() - to.getLocX()) * (from.getLocX() - to.getLocX()))
				+ ((from.getLocY() - to.getLocY()) * (from.getLocY() - to.getLocY())));
	}

	private static float minValueCostFromStartFromOpenList(List<Node> openList) {
		float minVal = Float.MIN_VALUE;
		for (Node node : openList) {
			if (node.costFromStart < minVal)
				minVal = node.costFromStart;
		}
		return minVal;
	}
	
	private static List<LocalTime> generateSchedule(Line line, Stop stop, boolean direction, Map<Integer, Stop> allStops) {
		int timeShift = 0;
		if (!line.getRoute().contains(stop.getId())) {
			throw new RuntimeException("Stop is not in line route!");
		}
		if (direction) {
			Stop lastStop = allStops.get(line.getRoute().get(0));
			for (int i = 1; !lastStop.getId().equals(stop.getId()); ++i) {
				Stop currentStop = allStops.get(line.getRoute().get(i));

				timeShift += timeBetweenStops(currentStop, lastStop);
				lastStop = currentStop;
			}
		} else {
			Stop lastStop = allStops.get(line.getRoute().get(line.getRoute().size() - 1));
			for (int i = line.getRoute().size() - 2; !lastStop.getId().equals(stop.getId()); --i) {
				Stop currentStop = allStops.get(line.getRoute().get(i));

				timeShift += timeBetweenStops(currentStop, lastStop);
				lastStop = currentStop;
			}
		}
		
		LocalTime firstFromStop = line.getFirst().toLocalTime().plusMinutes(timeShift);
		LocalTime lastFromStop = line.getLast().toLocalTime().plusMinutes(timeShift);
		
		LocalTime morningPeakStartOnStop = Constants.MORNING_PEAK_START.plusMinutes(timeShift);
		LocalTime morningPeakEndOnStop = Constants.MORNING_PEAK_END.plusMinutes(timeShift);
		LocalTime afternoonPeakStartOnStop = Constants.AFTERNOON_PEAK_START.plusMinutes(timeShift);
		LocalTime afternoonPeakEndOnStop = Constants.AFTERNOON_PEAK_END.plusMinutes(timeShift);
		
		List<LocalTime> schedule = new ArrayList<LocalTime>();
		for (LocalTime currTime = firstFromStop; currTime.isBefore(lastFromStop); ) {
			schedule.add(currTime);

			if ((currTime.isAfter(morningPeakStartOnStop) && currTime.isBefore(morningPeakEndOnStop)) ||
				(currTime.isAfter(afternoonPeakStartOnStop) && currTime.isBefore(afternoonPeakEndOnStop))) {
				currTime = currTime.plusMinutes((long)line.getFPeak().intValue());
			} else {
				currTime = currTime.plusMinutes((long)line.getFNotPeak().intValue());
			}
		}

		return schedule;
	}
	
	private static int timeBetweenStops(Stop from, Stop to) {
		return from.getTimes().get(from.getConns().indexOf(to.getId()));
	}
	
	private static Boolean getDirection(Line line, Stop firstStop, Stop lastStop) {
		return line.getRoute().indexOf(firstStop.getId()) < line.getRoute().indexOf(lastStop.getId());
	}
}
