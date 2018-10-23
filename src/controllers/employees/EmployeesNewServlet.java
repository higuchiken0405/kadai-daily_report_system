package controllers.employees;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;

@WebServlet("/employees/new")
public class EmployeesNewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesNewServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //セッションIDをトークンとしてJSPに渡す
        request.setAttribute("_token", request.getSession().getId());
        //EmployeeインスタンスをJSPに渡す
        request.setAttribute("employee", new Employee());
        //new.jspに移動
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
        rd.forward(request, response);
    }
}
