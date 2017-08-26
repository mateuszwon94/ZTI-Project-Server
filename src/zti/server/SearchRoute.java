package zti.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.*;
import zti.server.data.*;

/**
 * @author Mateusz Winiarski
 * Servlet odpowiedzialny za generowanie trasy przejazdu i wyswietlanie jej uzytkownikowi
 */
@WebServlet("/SearchRoute")
public class SearchRoute extends HttpServlet {	
    public SearchRoute() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Metoda wykonujaca rzadania GET protokolu HTTP
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		
		Integer from = Integer.valueOf(request.getParameterValues(Constants.FROM)[0]);
		Integer to = Integer.valueOf(request.getParameterValues(Constants.TO)[0]);
		LocalTime startTime = LocalTime.parse(request.getParameterValues(Constants.TIME)[0]);
		
		try {
			List<Stop> path = Util.generatePath(from, to);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = Util.generateRoute(path, startTime).toXml(doc, path);
			rootElement.setAttribute(Constants.XSI, Constants.XSI_VAL);
			rootElement.setAttribute(Constants.XSD, Constants.XSD_VAL);

			doc.appendChild(rootElement);
			
			Util.writeXmlToPrintWriter(doc, out);
		} catch ( Exception e ) {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.toString());
			Util.printException(e, out);
			throw new RuntimeException(e);
		} 
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * Metoda wykonujaca rzadania POST protokolu HTTP
	 * Przekazuje rzadanie do metody doGet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static final long serialVersionUID = 1L;
}
