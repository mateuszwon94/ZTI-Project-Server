package zti.server.util;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.data.*;
import zti.server.data.exception.PathNotFoundException;
import zti.server.sql.DataBaseConnection;

/**
 * @author Mateusz Winiarski
 * Klasa zawierajaca funkcje pomocnicze wykorzystywane w aplikacji
 */
public final class Util {
	/**
	 * Tworzy element XML z odpowiednia zawartoscia tekstowa
	 * @param doc dokument w jakim nalezy stworzyc element
	 * @param name nazwa elementu
	 * @param content wartosc elementu
	 * @return stworzony element XML
	 */
	public static Element createElement(Document doc, String name, String content) {
		Element elem = doc.createElement(name);
		elem.appendChild(doc.createTextNode(content));
		return elem;
	}

	/**
	 * Wypisywanie bledu na wyjscie
	 * @param e wyjatek
	 * @param out wyjscie 
	 */
	public static void printException(Exception e, PrintWriter out) {
		out.print(e);
		out.print("<br /><br />");
		for (StackTraceElement ste : e.getStackTrace()) {
			out.print(ste.toString());
			out.print("<br />");
		}
	}

	/**
	 * Wypisywanie dokumentu XML na wyjscie
	 * @param doc dokument
	 * @param out wyjscie
	 */
	public static void writeXmlToPrintWriter(Document doc, PrintWriter out) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

	/**
	 * implementacja algorytmu A*
	 * @param fromID ID przystanku poczatkowego
	 * @param toID ID przystanku docelowego
	 * @return Lista przystankow posrednich
	 * @throws PathNotFoundException jesli nie da sie dotrzec z przystanku poczatkowego do docelowego
	 */
	public static List<Stop> generatePath(Integer fromID, Integer toID)
			throws PathNotFoundException, ClassNotFoundException, SQLException {
		// pobranie przystankow i linii z serwera
		Map<Integer, Stop> stopMap = DataBaseConnection.getStopMap();

		return generatePath(fromID, toID, stopMap);
	}

	/**
	 * implementacja algorytmu A*
	 * @param fromID ID przystanku poczatkowego
	 * @param toID ID przystanku docelowego
	 * @param stopMap mapa z wszystkimi przystankami w miescie
	 * @return Lista przystankow posrednich
	 * @throws PathNotFoundException jesli nie da sie dotrzec z przystanku poczatkowego do docelowego
	 */
	public static List<Stop> generatePath(Integer fromID, Integer toID, Map<Integer, Stop> stopMap)
			throws PathNotFoundException, ClassNotFoundException, SQLException {
		if (fromID == null)
			throw new NullPointerException("fromID");
		else if (toID == null)
			throw new NullPointerException("toID");

		Stop from = stopMap.getOrDefault(fromID, null);
		Stop to = stopMap.getOrDefault(toID, null);
		if (from == null)
			throw new NullPointerException("from");
		else if (to == null)
			throw new NullPointerException("to");

		// jesli przystanku ze soba sasiaduja zwroc liste je zawierajaca
		if (stopMap.get(from.getId()).getConns().contains(to.getId()))
			return Arrays.asList(from, to);

		List<Node> openList = new ArrayList<Node>(Arrays.asList(new Node(from)));	// przystanki do przebadania
		List<Node> closeList = new ArrayList<Node>(stopMap.size());					// przystanki przebadane

		Node endNode = null;
		while (openList.size() != 0) {
			// wybranie wezla do badania
			Node current = openList.get(0);
			openList.remove(0);
			closeList.add(current);

			if (current.stop == to) { // jesli dotarto do przystanka docelowego
				endNode = current;
			} else if (endNode != null && minValueCostFromStartFromList(openList) < endNode.costFromStart) {
				return reconstructPath(endNode);
			}

			for (int i = 0; i < current.stop.getConns().size(); ++i) { // badanie polaczeni wychodzacych z przystanku
				Node newNode = new Node(stopMap.get(current.stop.getConns().get(i)), current);
				newNode.costFromStart = current.costFromStart + distance(current.stop, newNode.stop);
				newNode.costToEnd = distance(newNode.stop, to);

				if (closeList.contains(newNode)) { // przystanek byl badany
					Node oldNode = closeList.get(closeList.indexOf(newNode));
					if (newNode.costFromStart < oldNode.costFromStart) { // ale obecna sciezka jest lepsza
						closeList.remove(oldNode);
						openList.add(newNode);
					}
				} else if (openList.contains(newNode)) { // przystanek byl zobaczony
					Node oldNode = openList.get(openList.indexOf(newNode));
					if (newNode.costFromStart < oldNode.costFromStart) { // ale obecna sciezka jest lepsza
						oldNode.costFromStart = newNode.costFromStart;
						oldNode.parent = newNode.parent;
					}
				} else { // pierwsze odwiedziny przystanku
					openList.add(newNode);
				}
			}

			Collections.sort(openList);
		}

		// nie znaleziono sciezki
		throw new PathNotFoundException();
	}
	
	/**
	 * Funkcja generujaca rozklad jazdy
	 * @param lineNum linia dla ktorej wygenerowac nalezy rozklad
	 * @param stopId ID przystanku dla ktorego wygenerowac nalezy rozklad
	 * @return wygenerowany rozklad
	 */
	public static Schedule getSchedule(Integer lineNum, Integer stopId) throws ClassNotFoundException, SQLException {
		Line line = DataBaseConnection.getLine(lineNum);
		Map<Integer, Stop> allStops = DataBaseConnection.getStopMap();
		Stop stop = allStops.get(stopId);
		
		Schedule schedule = new Schedule();
		schedule.setLine(line);
		schedule.setStop(stop);
		Map<String, List<LocalTime>> times = new HashMap<String, List<LocalTime>>();
		if (line.getVariants() == null) { // linia nie ma ustalnych nazw wariantow
			times.put("0", generateSchedule(line, stop, true, allStops));
			times.put("1", generateSchedule(line, stop, false, allStops));
		} else {
			times.put(line.getVariants().get(0), generateSchedule(line, stop, true, allStops));
			times.put(line.getVariants().get(1), generateSchedule(line, stop, false, allStops));
		}
		schedule.setTimes(times);
		
		return schedule;
	}
	
	/**
	 * @param stop badany przystanek
	 * @param allLines wszystkie dostepne linie
	 * @return Liste lini przejezdzajacych przez ten przystanek
	 */
	public static List<Line> getAllLineOnStop(Stop stop, Map<Integer, Line> allLines) {
		List<Line> lines = new ArrayList<Line>();
		
		for (Map.Entry<Integer, Line> lineEntry : allLines.entrySet()) {
			if (lineEntry.getValue().getRoute().contains(stop.getId())) {
				lines.add(lineEntry.getValue());
			}
		}
		
		return lines;
	}
	
	/**
	 * Generuje trase przejazdu na podstawie sciezki i czasu odjazdu
	 * @param path sciezka trasy
	 * @param startTime czas rozpoczecia podrozy
	 * @return wygenerowana trasa
	 */
	public static Route generateRoute(List<Stop> path, LocalTime startTime) 
			throws ClassNotFoundException, SQLException {
		Map<Integer, Line> allLines = DataBaseConnection.getLineMap();
		Map<Integer, Stop> allStops = DataBaseConnection.getStopMap();
	
		return generateRoute(path, startTime, allLines, allStops);
	}
	
	/**
	 * Generuje trase przejazdu na podstawie sciezki i czasu odjazdu
	 * @param path sciezka trasy
	 * @param startTime czas rozpoczecia podrozy
	 * @param allLines wszystkie dostepne linie w miescie
	 * @param allStops wszystkie dostepne przystanki w miescie
	 * @return wygenerowana trasa
	 */
	public static Route generateRoute(List<Stop> path, LocalTime startTime, Map<Integer, Line> allLines, Map<Integer, Stop> allStops)
			throws ClassNotFoundException, SQLException {
		Map<Stop, Pair<Line, LocalTime>> route = new HashMap<Stop, Pair<Line, LocalTime>>();
		Line currentLine = null;
		LocalTime currentTime = LocalTime.of(startTime.getHour(), startTime.getMinute());
		int currentScheduleIndex = -1;
		boolean currentDirection = true;
		for (int i = 0; i < path.size(); ++i) {
			Stop currentStop = path.get(i);

			List<Line> linesOnStop = getAllLineOnStop(currentStop, allLines);
			if (currentLine == null || (i < path.size() -1 && !currentLine.getRoute().contains(path.get(i + 1)))) {  
				//jesli poczatek lub linia nie jedzie dalej po trasie
				Map<Line, Boolean> interestingLines = new HashMap<Line, Boolean>();
				for (Line line : linesOnStop) {
					if (line.getRoute().contains(path.get(i + 1).getId())) {
						interestingLines.put(line, getDirection(line, currentStop, path.get(i + 1)));
					}
				}

				Map<Line, Pair<LocalTime, Integer>> lineFirstDeparture = new HashMap<Line, Pair<LocalTime, Integer>>();
				for (Map.Entry<Line, Boolean> entry : interestingLines.entrySet()) {
					// szukanie najblizszego odjazdu lini poruszajacej sie w dobrym kierunku
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
						// ustawienie najblizszego odjazdu ze wszystkich lini w tym kierunku
						minTime = entry.getValue().getKey();
						currentLine = entry.getKey();
						currentScheduleIndex = entry.getValue().getValue();
						currentDirection = getDirection(currentLine, currentStop, path.get(i + 1));
						currentTime = entry.getValue().getKey();
					}
				}

				route.put(currentStop, new Pair<Line, LocalTime>(currentLine, currentTime));
				
			} else { // jesli mozna jechac dalej ta linia
				List<LocalTime> lineSchedule = generateSchedule(currentLine, currentStop, currentDirection, allStops);
				currentTime = lineSchedule.get(currentScheduleIndex);
				route.put(currentStop, new Pair<Line, LocalTime>(currentLine, currentTime));
			}
		}

		return new Route(route);
	}

	/**
	 * Funkcja rekonstruujaca sciezka na podstawie hierarchi wezlow
	 * @param node wezel koncowy sciezki
	 * @return zrekonstruowana sciezka
	 */
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

	/**
	 * @param from pierwszy przystanek
	 * @param to drugi przystanek
	 * @return dystans pomiedzy dwoma przystankamia
	 */
	private static float distance(Stop from, Stop to) {
		return (float) Math.sqrt(((from.getLocX() - to.getLocX()) * (from.getLocX() - to.getLocX()))
				+ ((from.getLocY() - to.getLocY()) * (from.getLocY() - to.getLocY())));
	}

	/**
	 * @param list lista do przebadania
	 * @return najmniejsza wartosc z listy
	 */
	private static float minValueCostFromStartFromList(List<Node> list) {
		float minVal = Float.MIN_VALUE;
		for (Node node : list) {
			if (node.costFromStart < minVal)
				minVal = node.costFromStart;
		}
		return minVal;
	}
	
	/**
	 * Funkcja generujaca rozklad jazdy
	 * @param line linia dla jakiej nalezy eygenerowac rozklad
	 * @param stop przystanek dla jakiego nalezy wygenerowac rozklad
	 * @param direction kierunek poruszania sie linii
	 * @param allStops wszystkie dostepne przystanki
	 * @return wygenerowany rozklad
	 */
	private static List<LocalTime> generateSchedule(Line line, Stop stop, boolean direction, Map<Integer, Stop> allStops) {
		int timeShift = 0;
		if (!line.getRoute().contains(stop.getId())) {
			throw new RuntimeException("Stop is not in line route!");
		}
		
		// Ustalenie czasu przejazdu od pierwszego przystanku do badanego
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
		
		// przesuniecie czasow potrzebnych do wygenerowania czasow odjazdow z badanego przystanku
		LocalTime firstFromStop = line.getFirst().toLocalTime().plusMinutes(timeShift);
		LocalTime lastFromStop = line.getLast().toLocalTime().plusMinutes(timeShift);
		
		LocalTime morningPeakStartOnStop = Constants.MORNING_PEAK_START.plusMinutes(timeShift);
		LocalTime morningPeakEndOnStop = Constants.MORNING_PEAK_END.plusMinutes(timeShift);
		LocalTime afternoonPeakStartOnStop = Constants.AFTERNOON_PEAK_START.plusMinutes(timeShift);
		LocalTime afternoonPeakEndOnStop = Constants.AFTERNOON_PEAK_END.plusMinutes(timeShift);
		
		// wygenerowanie kolejnych czasow odjazdow
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
	
	/**
	 * @param from pierwszy przystanek
	 * @param to drugi przystanek
	 * @return czas przejazdu pomiedzy przystankami
	 */
	private static int timeBetweenStops(Stop from, Stop to) {
		return from.getTimes().get(from.getConns().indexOf(to.getId()));
	}
	
	/**
	 * Okresla kierunek linii na podstawei polozenia na trasie dwoch przystankow
	 * @param line linia
	 * @param firstStop pierwszy przystanek
	 * @param lastStop drugi przystanek
	 * @return kierunek linii
	 */
	private static Boolean getDirection(Line line, Stop firstStop, Stop lastStop) {
		return line.getRoute().indexOf(firstStop.getId()) < line.getRoute().indexOf(lastStop.getId());
	}
}
