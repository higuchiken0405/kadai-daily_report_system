package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

@WebServlet("/ReportShowServlet")
public class ReportShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportShowServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
	    EntityManager em = DBUtil.createEntityManager();
	    //パラメータから取得したIDの値で検索した結果を格納
	    Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
	    //エンティティマネージャを終了
	    em.close();

	    //検索結果とセッションIDをJSPに渡す
	    request.setAttribute("report", r);
	    request.setAttribute("_token", request.getSession().getId());

	    //show.jspに移動
	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
	    rd.forward(request, response);
	}


}
