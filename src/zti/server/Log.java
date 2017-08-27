package zti.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.data.Constants;
import zti.server.util.Logger;
import zti.server.util.Util;

/**
 * @author Mateusz Winiarski
 * Servlet odpowiedzialny za wyswietlanie logow uzytkownikowi
 */
@WebServlet("/Log")
public class Log extends HttpServlet {
	public Log() {
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

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(Constants.LOGS);
			
			for (Map.Entry<LocalDateTime, String> logEntry : Logger.getLogs().entrySet()) {
				Element logElement = Util.createElement(doc, Constants.LOG, logEntry.getValue());
				logElement.setAttribute(Constants.TIME, logEntry.getKey().format(dtf));

				rootElement.appendChild(logElement);
			}

			rootElement.setAttribute(Constants.XSI, Constants.XSI_VAL);
			rootElement.setAttribute(Constants.XSD, Constants.XSD_VAL);
			doc.appendChild(rootElement);
			Util.writeXmlToPrintWriter(doc, out);
		} catch (Exception e) {
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

	/**
	 * Format w jakim wyswietlana jest data w logu
	 */
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final long serialVersionUID = 1L;
}
