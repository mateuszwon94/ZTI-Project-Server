package zti.server;

import javax.servlet.http.HttpServlet;

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
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
import zti.server.sql.*;

@WebServlet("/Schedules")
public class Schedules extends HttpServlet {
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Schedules() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static final long serialVersionUID = 1L;
}