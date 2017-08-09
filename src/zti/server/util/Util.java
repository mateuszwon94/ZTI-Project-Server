package zti.server.util;

import java.io.PrintWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
}
