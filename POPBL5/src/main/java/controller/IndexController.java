package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class will show the main page (as index_page.jsp is not directly accessible).
 * Be careful, becouse an index.jsp file will make this controller unaccessible.
 * @author aperez
 *
 */
@WebServlet("/index.html")
public class IndexController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public IndexController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("Index Controller");
    RequestDispatcher dispatcher = getServletContext()
        .getRequestDispatcher("/home.jsp");
    dispatcher.forward(request, response);
      
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
