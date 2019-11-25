package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.ControllerHelper;

/**
 * This class will show the error page (as error.jsp is not directly accessible).
 * @author aperez
 *
 */
@WebServlet("/error/*")
public class ErrorController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ErrorController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ControllerHelper helper = new ControllerHelper(request);
    int errorCode = helper.getId();
    if(errorCode < 0) errorCode = HttpServletResponse.SC_NOT_FOUND;
    
    request.setAttribute("errorCode", errorCode);
    
    System.out.println("Error Controller");
    RequestDispatcher dispatcher = getServletContext()
        .getRequestDispatcher("/pages/error.jsp");
    dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
