package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.user.dao.UserFacade;
import domain.user.model.User;
import helper.ControllerHelper;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public UserController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  ControllerHelper helper = new ControllerHelper(request);
	  String action = helper.getAction();
		
	  switch(action) {
	    case "delete":
	      deleteUser(request, response, helper.getId());
	      break;
	    case "create":
	    case "edit":
	      showUserForm(request, response, helper.getId());
	      break;
	    case "view":
	      showUser(request, response, helper.getId());
	      break;
	    case "list":
      default:
        listUsers(request, response);
	  }
	}
	
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ControllerHelper helper = new ControllerHelper(request);
    String action = helper.getAction();
    
    switch(action) {
      case "create":
        createUser(request, response);
        break;
      case "edit":
        editUser(request, response, helper.getId());
        break;
      default:
        listUsers(request, response);
    }
  }
  private void deleteUser(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
    HttpSession session = request.getSession(true);

    UserFacade uf = new UserFacade();
    if(userId != -1 && uf.deleteUser(userId)) {
      session.setAttribute("message", "message.deleteUser");
      session.removeAttribute("user");
    }else {
      session.setAttribute("error", "error.deleteUser");
    }
    response.sendRedirect(request.getContextPath());
  }
  
	private void editUser(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
    HttpSession session = request.getSession(true);

    UserFacade uf = new UserFacade();
    User user = uf.loadUser(userId);
    System.out.println("Show User Form: "+userId);
    if(user != null) {
      user.setEmail(request.getParameter("email"));
      user.setFirstName(request.getParameter("firstName"));
      user.setPassword(request.getParameter("password"));
      user.setSecondName(request.getParameter("secondName"));
      user.setUsername(request.getParameter("username"));
      uf.saveUser(user);
      if(user.getUserId() != 0) {
        session.setAttribute("message", "message.editUser");
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath()+"/user/"+userId);
      }else {
        session.setAttribute("error", "error.editUser");
        //response.sendRedirect("user?action=edit&userId="+userId);
        response.sendRedirect(request.getContextPath()+"/user/"+userId+"/edit");
      }
    }else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    User user = new User();
    user.setEmail(request.getParameter("email"));
    user.setFirstName(request.getParameter("firstName"));
    user.setPassword(request.getParameter("password"));
    user.setSecondName(request.getParameter("secondName"));
    user.setUsername(request.getParameter("username"));
    
    UserFacade uf = new UserFacade();
    uf.saveUser(user);
    

    User sessionUser = (User) session.getAttribute("user");
    String redirectUrl = "";
    if(user.getUserId() != 0) {
      request.setAttribute("user", user);
      session.setAttribute("message", "message.createUser");
      if(sessionUser!=null)
        redirectUrl = "/user/"+user.getUserId();
    }else {
      session.setAttribute("error", "error.createUser");
      if(sessionUser != null)
        redirectUrl = "/user";
    }
    response.sendRedirect(request.getContextPath()+redirectUrl);
  }

  private void showUser(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
    System.out.println("Show User: "+userId);
    UserFacade uf = new UserFacade();
    User user = uf.loadUser(userId);

    if(user!=null && user.getUserId() != 0) {
      request.setAttribute("user", user);

      RequestDispatcher dispatcher = getServletContext()
              .getRequestDispatcher("/pages/user/user.jsp");
      dispatcher.forward(request, response);
    }else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "User Not Found: "+userId);
    }
    
  }

  private void showUserForm(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
    System.out.println("Show User Form: "+userId);
    if(userId > 0) {
      // Edit user
      UserFacade userFacade = new UserFacade();
      User user = userFacade.loadUser(userId);
      request.setAttribute("user", user);
    }// Else, Create user

    RequestDispatcher dispatcher = getServletContext()
            .getRequestDispatcher("/pages/user/user_form.jsp");
    dispatcher.forward(request, response);
  }

  private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("List Users");
    UserFacade userFacade = new UserFacade();
    ArrayList<User> users = userFacade.loadUsers();
    request.setAttribute("userList", users);

    RequestDispatcher dispatcher = getServletContext()
            .getRequestDispatcher("/pages/user/user_list.jsp");
    dispatcher.forward(request, response);
  }

}
