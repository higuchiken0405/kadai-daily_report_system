package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

@WebServlet("/reports/edit")
public class ReportsEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportsEditServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
	    EntityManager em = DBUtil.createEntityManager();
	    //パラメータから取得したIDの値で検索した結果を格納
	    Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
	    //エンティティマネージャを終了
	    em.close();
	    //セッションからログイン中の従業員のデータを取得
	    Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

	    if(login_employee.getId() == r.getEmployee().getId()) {
	        //ログイン中の従業員のIDと検索した結果の従業員のIDが等しい場合、
	        //日報、セッションIDとJSPに渡す
	        request.setAttribute("report", r);
	        request.setAttribute("_token", request.getSession().getId());
	        //セッションに日報のIDを格納
	        request.getSession().setAttribute("report_id", r.getId());
	    }
	    //edit.jspに移動
	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
	    rd.forward(request, response);
	}

}
