package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesEditServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
        EntityManager em = DBUtil.createEntityManager();
        //パラメータから取得したIDの値で検索した結果を格納
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //エンティティマネージャを終了
        em.close();
        //Employee(検索結果)、セッションIDをJSPに保存
        request.setAttribute("employee", e);
        request.setAttribute("_token", request.getSession().getId());
        //EmployeeのIDをセッションに保存
        request.getSession().setAttribute("employee_id", e.getId());
        //edit.jspに移動
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        rd.forward(request, response);
	}
}