package zti.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
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

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// Class.forName("org.apache.derby.jdbc.ClientDriver");
			// url = "jdbc:postgresql://host:port/database"
			String url = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/xggfrvfc";
			String username = "xggfrvfc";
			String password = "q1gFyHQUPS0ZkVzS9nqmlshn0CzDNGgC";
			String sql = "SELECT * FROM public.stops";
			
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			
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
				stopElement.appendChild(createElement(doc, NAME, rs.getString(NAME)));
				stopElement.appendChild(createElement(doc, NZ, Boolean.toString(rs.getBoolean(NZ))));
				stopElement.appendChild(createElement(doc, LOC_X, Float.toString(rs.getFloat(LOC_X))));
				stopElement.appendChild(createElement(doc, LOC_Y, Float.toString(rs.getFloat(LOC_Y))));
				
				Element connsElement = doc.createElement(CONNS);
				for (Integer connection : (Integer[])(rs.getArray(CONN).getArray())) {
					connsElement.appendChild(createElement(doc, CONN, connection.toString()));
				}
				stopElement.appendChild(connsElement);
				
				rootElement.appendChild(stopElement);
			}
			
			doc.appendChild(rootElement);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch ( Exception e ) {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.toString());
			printException(e, out);
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch ( Exception e ) {
				printException(e, out);
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private Element createElement(Document doc, String name, String content) {
		Element elem = doc.createElement(name);
		elem.appendChild(doc.createTextNode(content));
		return elem;
	}
	
	private void printException(Exception e, PrintWriter out) {
		out.print(e);
		out.print("<br /><br />");
		for (StackTraceElement ste : e.getStackTrace()) {
			out.print(ste.toString());
			out.print("<br />");
		}	
	}
}
