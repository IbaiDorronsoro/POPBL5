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

import domain.user.model.User;
import helper.ControllerHelper;

/**
 * Servlet Filter implementation class UserFilter
 */
@WebFilter("/user/*")
public class UserFilter implements Filter {
    public UserFilter() { }
	public void destroy() {}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	  System.out.println("User Filter");
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    HttpSession session = request.getSession(true);
    User user = (User) session.getAttribute("user");
    
    ControllerHelper helper = new ControllerHelper(request);
    String action = helper.getAction();
    int errorCode;
    
    switch(action) {
    case "create":
      System.out.println("User creation allowed for anybody.");
      errorCode = HttpServletResponse.SC_OK;
      break;
    case "edit":
    case "delete":
      errorCode = filterModification(helper, session, user);
      break;
    case "view":
    case "list":
      errorCode = filterShow(session, user);
      break;
    default:
      System.out.println("Unknown User action: "+action);
      session.setAttribute("error", "error.400.unknown_action");
      errorCode = HttpServletResponse.SC_BAD_REQUEST;
    }

    if(errorCode == HttpServletResponse.SC_OK) {
      chain.doFilter(req, res);
    }else {
      response.sendRedirect(request.getContextPath()+"/error/"+errorCode);
    }
	}
  public void init(FilterConfig fConfig) throws ServletException {}


  private int filterModification(ControllerHelper helper, HttpSession session, User user) {
    int errorCode;
    if(user!= null && user.getUserId() == helper.getId()) {
      System.out.println("User modifies it's own user.");
      errorCode = HttpServletResponse.SC_OK;
    }else {
      System.out.println("User trying to modify other user.");
      errorCode = HttpServletResponse.SC_FORBIDDEN;
      session.setAttribute("error", "error.403.not_own_user");
    }
    return errorCode;
  }
  

  private int filterShow(HttpSession session, User user) {
    int errorCode;
    if(user!=null) {
      System.out.println("User in session. They can view/list user(s).");
      errorCode = HttpServletResponse.SC_OK;
    }else {
      System.out.println("View/List actions need the user to be in session.");
      errorCode = HttpServletResponse.SC_FORBIDDEN;
      session.setAttribute("error", "error.403.not_session_user");
    }
    return errorCode;
  }
}
