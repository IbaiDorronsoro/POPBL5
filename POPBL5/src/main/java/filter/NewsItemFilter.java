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

import domain.news.dao.NewsItemFacade;
import domain.news.model.NewsItem;
import domain.user.model.User;
import helper.ControllerHelper;

@WebFilter("/news/*")
public class NewsItemFilter implements Filter {

    public NewsItemFilter() { }
	public void destroy() {}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	  System.out.println("NewsItem Filter");
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    HttpSession session = request.getSession(true);
    User user = (User) session.getAttribute("user");
    
    ControllerHelper helper = new ControllerHelper(request);
    String action = helper.getAction();
    int errorCode;
    
    switch(action) {
    case "create":
      errorCode = filterCreation(session, user);
      break;
    case "edit":
    case "delete":
      errorCode = filterModification(helper, session, request, user);
      break;
    case "view":
    case "list":
      System.out.println("Even if no user in session, view and list permited: "+action);
      errorCode = HttpServletResponse.SC_OK;
      break;
    default:
      System.out.println("Unknown News Item action: "+action);
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

  private int filterCreation(HttpSession session, User user) {
    int errorCode;
    if(user != null) {
      System.out.println("News Item creation allowed for users: "+user.getUserId());
      errorCode = HttpServletResponse.SC_OK;
    }else {
      System.out.println("News Item creation needs a user in session.");
      errorCode = HttpServletResponse.SC_FORBIDDEN;
      session.setAttribute("error", "error.403.not_session_user");
    }
    return errorCode;
  }
  private int filterModification(ControllerHelper helper, HttpSession session, HttpServletRequest request, User user) {
    int errorCode;
    int newsItemId = helper.getId();
    NewsItemFacade nif = new NewsItemFacade();
    NewsItem newsItem = nif.loadNewsItem(newsItemId);
    if(newsItem.getNewsItemId() < 0) {
      errorCode = HttpServletResponse.SC_NOT_FOUND;
      session.setAttribute("error", "error.404.news_item_not_found");
    }else if(user != null && user.getUserId() == newsItem.getAuthor().getUserId()){
      request.setAttribute("newsItem", newsItem);
      errorCode = HttpServletResponse.SC_OK;
    }else {
      errorCode = HttpServletResponse.SC_FORBIDDEN;
      session.setAttribute("error", "error.403.user_not_author");
    }
    return errorCode;
  }

}
