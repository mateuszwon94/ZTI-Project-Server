package zti.server.data;

import java.time.LocalTime;
import java.util.*;
import java.io.Serializable;
import javax.persistence.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Pair;
import zti.server.sql.DataBaseConnection;
import zti.server.util.Util;

public class Route {
	public Route() { }
	public Route(Map<Stop, Pair<Line, LocalTime>> connections) { this.connections = connections; }
	
	public Map<Stop, Pair<Line, LocalTime>> getConnections() { return connections; }
	public void setConnections(Map<Stop, Pair<Line, LocalTime>> connections) { this.connections = connections; }
	
	public Element toXml(Document doc, List<Stop> path) {
		Element routeElement = doc.createElement(Constants.ROUTE);

		for (Stop stop : path) {
			Element lineElement = doc.createElement(Constants.STOP);
			lineElement.setAttribute(Constants.ID, stop.getId().toString());
			lineElement.appendChild(Util.createElement(doc, Constants.LINE, connections.get(stop).getKey().getNumber().toString()));
			lineElement.appendChild(Util.createElement(doc, Constants.TIME, connections.get(stop).getValue().toString()));

			routeElement.appendChild(lineElement);
		}

		return routeElement;
	}
	
	private Map<Stop, Pair<Line, LocalTime>> connections;
}
