package zti.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mateusz Winiarski
 * Servlet bedacy indeksem projektu
 */
@WebServlet("/ProjIndex")
public class ProjIndex extends HttpServlet {
	public ProjIndex() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Metoda wykonujaca rzadania GET protokolu HTTP
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<h1>Mateusz Winiarski</h1>");
		out.println("<h2>ZTI - 2017</h2>");
		out.println("<p>Projekt wyszukiwarki polaczen tramwajowych</p>");
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
