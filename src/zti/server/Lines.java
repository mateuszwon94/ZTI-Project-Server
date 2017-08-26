package zti.server;

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

/**
 * @author Mateusz Winiarski
 * Servlet odpowiedzialny za odczytywanie linii z bazy danych i wyswietlanie ich uzytkownikowi
 */
@WebServlet("/Lines")
public class Lines extends HttpServlet {
	public Lines() {
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
		
		Integer reqNum = null;
		try {
			reqNum = Integer.valueOf(request.getParameterValues(Constants.LINE)[0]);
		} catch (NullPointerException ex) { }
		
		try {			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = null;
			if (reqNum == null) {
				rootElement = doc.createElement(Constants.LINES);
				
				for ( Map.Entry<Integer, Line> lineEntry : DataBaseConnection.getLineMap().entrySet() ) {
					rootElement.appendChild(lineEntry.getValue().toXml(doc));
				}
			} else {
				rootElement = DataBaseConnection.getLine(reqNum).toXml(doc);
			}

			rootElement.setAttribute(Constants.XSI, Constants.XSI_VAL);
			rootElement.setAttribute(Constants.XSD, Constants.XSD_VAL);
			doc.appendChild(rootElement);
			Util.writeXmlToPrintWriter(doc, out);
		} catch ( Exception e ) {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.toString());
			Util.printException(e, out);
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
