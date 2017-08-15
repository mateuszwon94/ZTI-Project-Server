package zti.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

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
 * Servlet implementation class SearchRoute
 */
@WebServlet("/SearchRoute")
public class SearchRoute extends HttpServlet {	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchRoute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		
		Integer from = Integer.valueOf(request.getParameterValues(Constants.FROM)[0]);
		Integer to = Integer.valueOf(request.getParameterValues(Constants.TO)[0]);
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(Constants.ROUTE);
			rootElement.setAttribute(Constants.XSI, Constants.XSI_VAL);
			rootElement.setAttribute(Constants.XSD, Constants.XSD_VAL);
			
			for (Stop stop : Util.generateRoute(from, to)) {
				Element stopElement = doc.createElement(Constants.STOP);
				stopElement.setAttribute(Constants.ID, stop.getId().toString());
				rootElement.appendChild(stopElement);
			}
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
		doGet(request, response);
	}

	private static final long serialVersionUID = 1L;
}
