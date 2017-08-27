package zti.server;

import javax.servlet.http.HttpServlet;

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.*;
import zti.server.data.*;
import zti.server.sql.*;

/**
 * @author Mateusz Winiarski
 * Servlet odpowiedzialny za generowanie rozkladow jazdy i wyswietlanie ich uzytkownikowi
 */
@WebServlet("/Schedules")
public class Schedules extends HttpServlet {
    public Schedules() {
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
		
		Integer lineNum = null;
		Integer stopId = null;
		try {
			lineNum = Integer.valueOf(request.getParameterValues(Constants.LINE)[0]);
		} catch (NullPointerException ex) { }
		try {
			stopId = Integer.valueOf(request.getParameterValues(Constants.STOP)[0]);
		} catch (NullPointerException ex) { }
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = null;
			if (lineNum != null && stopId != null) {
				rootElement = Util.getSchedule(lineNum, stopId).toXml(doc);
			} else if (stopId == null) {
				rootElement = doc.createElement(Constants.SCHEDULES);
				for (Integer stop : DataBaseConnection.getLine(lineNum).getRoute() ) {
					rootElement.appendChild(Util.getSchedule(lineNum, stop).toXml(doc));
				}
			} else { //lineId == null
				rootElement = doc.createElement(Constants.SCHEDULES);
				Map<Integer, Line> allLines = DataBaseConnection.getLineMap();
				Stop stop = DataBaseConnection.getStop(stopId);
				
				for (Line line : Util.getAllLineOnStop(stop, allLines) ) {
					rootElement.appendChild(Util.getSchedule(line.getNumber(), stopId).toXml(doc));
				}
			}
			
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
