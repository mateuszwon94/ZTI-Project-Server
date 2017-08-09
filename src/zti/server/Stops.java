package zti.server;

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
import zti.server.sql.*;

@WebServlet("/Stops")
public class Stops extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String ROOT 	= "stops";
	private final String STOP 	= "stop";
	private final String ID 	= "id";
	private final String NAME 	= "name";
	private final String NZ 	= "nz";
	private final String LOC_X 	= "loc_x";
	private final String LOC_Y 	= "loc_y";
	private final String CONNS 	= "conns";
	private final String CONN 	= "conn";

	public Stops() {
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		
		ResultSet rs = null;
		try {
			rs = DataBaseConnection.createStatement().executeQuery(DataBaseConnection.GET_ALL_STOPS);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(ROOT);
			rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
			
			while ( rs.next() ) {
				Element stopElement = doc.createElement(STOP);
				
				stopElement.setAttribute(ID, Integer.toString(rs.getInt(ID)));
				stopElement.appendChild(Util.createElement(doc, NAME, rs.getString(NAME)));
				stopElement.appendChild(Util.createElement(doc, NZ, Boolean.toString(rs.getBoolean(NZ))));
				stopElement.appendChild(Util.createElement(doc, LOC_X, Float.toString(rs.getFloat(LOC_X))));
				stopElement.appendChild(Util.createElement(doc, LOC_Y, Float.toString(rs.getFloat(LOC_Y))));
				
				Element connsElement = doc.createElement(CONNS);
				for (Integer connection : (Integer[])(rs.getArray(CONN).getArray())) {
					connsElement.appendChild(Util.createElement(doc, CONN, connection.toString()));
				}
				stopElement.appendChild(connsElement);
				
				rootElement.appendChild(stopElement);
			}
			
			doc.appendChild(rootElement);
			
			Util.writeXmlToPrintWriter(doc, out);
		} catch ( Exception e ) {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.toString());
			Util.printException(e, out);
		} finally {
			try {
				if (rs != null) rs.close();
			} catch ( Exception e ) {
				Util.printException(e, out);
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
}
