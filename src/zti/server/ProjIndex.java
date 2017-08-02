package zti.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProjIndex
 */
@WebServlet("/ProjIndex")
public class ProjIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjIndex() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<h1>Stops information</h1>");

		try {
			// Class.forName("org.apache.derby.jdbc.ClientDriver");
			// url = "jdbc:postgresql://host:port/database"
			String url = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/xggfrvfc";
			String username = "xggfrvfc";
			String password = "q1gFyHQUPS0ZkVzS9nqmlshn0CzDNGgC";
			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM public.stops";
			ResultSet rs = stmt.executeQuery(sql);
			out.println("<table>");
			out.println("<tr><th>id</th><th>name</th><th>nz</th><th>loc_x</th><th>loc_y</th></tr>");
			while ( rs.next() ) {
				out.print("<tr>");
				out.print("<td>" + rs.getInt("id") + "</td>");
				out.print("<td>" + rs.getString("name") + "</td>");
				out.print("<td>" + rs.getBoolean("nz") + "</td>");
				out.print("<td>" + rs.getFloat("loc_x") + "</td>");
				out.print("<td>" + rs.getFloat("loc_y") + "</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			rs.close();
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
