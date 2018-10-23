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

@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesShowServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //エンティティマネージャの生成
        EntityManager em = DBUtil.createEntityManager();
        //パラメータから取得したIDで検索した結果を格納
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //エンティティマネージャを終了
        em.close();
        //検索結果をJSPに渡す
        request.setAttribute("employee", e);
        //セッションIDをトークンとしてJSPに渡す
        request.setAttribute("_token", request.getSession().getId());
        //show.jspに移動
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
        rd.forward(request, response);
	}
}
