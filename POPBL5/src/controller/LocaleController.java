package controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.*;

@WebServlet("/LocaleController")
public class LocaleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LocaleController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String language = request.getParameter("language");
		String country = request.getParameter("country");
		Locale newLocale;
		if(language != null && !language.equals("") && country != null && !country.equals("")){
			newLocale = new Locale(language,country);
			Config.set(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALE, newLocale);
		}
		//response.sendRedirect(response.encodeRedirectURL("index.jsp"));
		response.sendRedirect(request.getHeader("referer"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
