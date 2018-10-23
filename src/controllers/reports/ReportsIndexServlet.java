package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportsIndexServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
	    EntityManager em = DBUtil.createEntityManager();
	    //ページネーション
	    //ページ番号を定義
	    int page;
	    try {
	        page = Integer.parseInt(request.getParameter("page"));
	    } catch(Exception e) {
	        page = 1;
	    }
	    //「日報の全てのデータを取得する」クエリを実行した結果を格納
	    List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
	                                            .setFirstResult(15 * (page -1))
	                                            .setMaxResults(15)
	                                            .getResultList();
	    //「日報の全データ数を取得する」クエリを実行した結果を格納
	    long reports_count = (long) em.createNamedQuery("getReportsCount", Long.class)
	                                            .getSingleResult();
	    //エンティティマネージャを終了
	    em.close();
	    //日報の全データ、全データ数、ページ番号をJSPに渡す
	    request.setAttribute("repots", reports);
	    request.setAttribute("reports_count", reports_count);
	    request.setAttribute("page", page);

	    if(request.getSession().getAttribute("flush") != null) {
	        //セッションにフラッシュメッセージがある場合
	        //セッションから取得したフラッシュメッセージをJSPに渡す
	        request.setAttribute("flush", request.getSession().getAttribute("flush"));
	        //セッションのフラッシュメッセージを削除
	        request.getSession().removeAttribute("flush");
	    }

	    //index.jspに移動
	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
	    rd.forward(request, response);
	}


}
