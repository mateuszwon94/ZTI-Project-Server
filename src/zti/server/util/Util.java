package zti.server.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

	public static List<Stop> generateRoute(Integer fromID, Integer toID) throws PathNotFoundException {
		if (fromID == null ) throw new NullPointerException("fromID");
		else if (toID == null) throw new NullPointerException("toID");
		
		try (PrintWriter writer = new PrintWriter("log.txt", "UTF-8")) {
			Map<Integer, Stop> stopMap = DataBaseConnection.getStopMap();
			Stop from = stopMap.getOrDefault(fromID, null);
			Stop to = stopMap.getOrDefault(toID, null);
			if (from == null )throw new NullPointerException("from");
			else if (to == null) throw new NullPointerException("to");
			
			writer.println("from " + from.getId() + " to "+ to.getId());
			
			if (stopMap.get(from.getId()).getConns().contains(to.getId())) 
				return Arrays.asList(from, to);
			
			List<Node> openList = new ArrayList<Node>(Arrays.asList(new Node(from)));
			List<Node> closeList = new ArrayList<Node>(stopMap.size());
			
			printList(writer, openList, "openList");
			printList(writer, closeList, "closeList");
			
			Node endNode = null;
			while (openList.size() != 0) {
				Node current = openList.get(0);
				openList.remove(0);
				closeList.add(current);
				
				writer.println("Current node is " + current.stop.getId());
				printList(writer, openList, "openList");
				printList(writer, closeList, "closeList");
				
				if ( current.stop == to ) { // znaleziono pole do którego dążyliśmy
					endNode = current;
					writer.println("EndNode was found!");
				} else if ( endNode != null && minValueCostFromStartFromOpenList(openList) < endNode.costFromStart ) {
					writer.println("Reconstructing path!");
					return reconstructPath(endNode);
				}
				
				for (int i = 0; i < current.stop.getConns().size(); ++i) {
					Node newNode = new Node(stopMap.get(current.stop.getConns().get(i)), current);
					newNode.costFromStart = current.costFromStart + distance(current.stop, newNode.stop);
					newNode.costToEnd = distance(newNode.stop, to);
					
					writer.println("Node " + newNode.stop.getId().toString() + " in connection with " + current.stop.getId().toString());
					
					if ( closeList.contains(newNode) ) {
						writer.println("closeList.contains(newNode)");
						Node oldNode = closeList.get(closeList.indexOf(newNode));
						if ( newNode.costFromStart < oldNode.costFromStart ) {
							writer.println("newNode.costFromStart (" + newNode.costFromStart + ") < (" + oldNode.costFromStart + ") oldNode.costFromStart");
							closeList.remove(oldNode);
							openList.add(newNode);
						}
					} else if ( openList.contains(newNode) ) {
						writer.println("openList.contains(newNode)");
						Node oldNode = openList.get(openList.indexOf(newNode));
						if ( newNode.costFromStart < oldNode.costFromStart ) {
							writer.println("newNode.costFromStart (" + newNode.costFromStart + ") < (" + oldNode.costFromStart + ") oldNode.costFromStart");
							oldNode.costFromStart = newNode.costFromStart;
							oldNode.parent = newNode.parent;
						}
					} else {
						writer.println("New node");
						openList.add(newNode);
					}
				}
				
				Collections.sort(openList);
				
				writer.flush();
			}
		} catch (IOException e) {
			   // do something
		}

		throw new PathNotFoundException();
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

	public static float distance(Stop from, Stop to) {
		return (float) Math.sqrt(((from.getLocX() - to.getLocX()) * (from.getLocX() - to.getLocX()))
				+ ((from.getLocY() - to.getLocY()) * (from.getLocY() - to.getLocY())));
	}

	public static float minValueCostFromStartFromOpenList(List<Node> openList) {
		float minVal = Float.MIN_VALUE;
		for (Node node : openList) {
			if (node.costFromStart < minVal)
				minVal = node.costFromStart;
		}
		return minVal;
	}
	
	private static void printList(PrintWriter out, List<Node> stops, String listName) {
		out.print(listName + " contains: ");
		for (Node node : stops) {
			out.print(node.stop.getId() + " ");
		}
		out.println();
	}
}
