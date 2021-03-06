package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpSession;

/**
 * This class will filter all direct calls to JSP pages.
 * @author aperez
 *
 */
@WebFilter("/*")
public class JSPPageFilter implements Filter {

  public JSPPageFilter() {
  }

	public void destroy() {
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    System.out.println("JSPPageFilter");
	  HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    HttpSession session = request.getSession();
    
    String requestPath = request.getRequestURI();
    
    if(requestPath.endsWith(".jsp")) {
      System.out.println("JSP file is being called. Do not allow.");
      session.setAttribute("error", "error.403.jsp");
      response.sendRedirect("error/"+HttpServletResponse.SC_FORBIDDEN);
    }else {
      System.out.println("No JSP file, continue with the request.");
      chain.doFilter(req, res);
    }
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
