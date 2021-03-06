package zti.server.data;

import java.time.LocalTime;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Pair;
import zti.server.util.Util;

/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca wyznaczona trase przejazdu
 */
public class Route {
	/**
	 * Konstruktor domyslny
	 */
	public Route() { }
	
	/**
	 * Konstruktor
	 * @param route trasa przejazdu
	 */
	public Route(Map<Stop, Pair<Line, LocalTime>> route) { this.route = route; }
	
	/**
	 * @return trasa przejazdu
	 */
	public Map<Stop, Pair<Line, LocalTime>> getRoute() { return route; }
	
	/**
	 * @param route nowa trasa przejazdu
	 */
	public void setRoute(Map<Stop, Pair<Line, LocalTime>> route) { this.route = route; }
	
	/**
	 * Przeksztalca obiekt do postaci elementu XML
	 * @param doc Dokument w jakim generowany jest XML
	 * @return wygenerowany element XML
	 */
	public Element toXml(Document doc, List<Stop> path) {
		Element routeElement = doc.createElement(Constants.ROUTE);

		for (Stop stop : path) {
			Element lineElement = doc.createElement(Constants.STOP);
			lineElement.setAttribute(Constants.ID, stop.getId().toString());
			lineElement.appendChild(Util.createElement(doc, Constants.LINE, route.get(stop).getKey().getNumber().toString()));
			lineElement.appendChild(Util.createElement(doc, Constants.TIME, route.get(stop).getValue().toString()));

			routeElement.appendChild(lineElement);
		}

		return routeElement;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Route)) return false;
		Route otherRoute = (Route)other;
		
		for (Map.Entry<Stop, Pair<Line, LocalTime>> entry : route.entrySet()) {
			if (!otherRoute.getRoute().keySet().contains(entry.getKey())) return false;
			if (!entry.getValue().equals(otherRoute.getRoute().get(entry.getKey()))) return false;
		}
		
		return true;
	}
	
	private Map<Stop, Pair<Line, LocalTime>> route;
}
