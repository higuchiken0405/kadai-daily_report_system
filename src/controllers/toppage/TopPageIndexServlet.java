package controllers.toppage;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TopPageIndexServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
	    EntityManager em = DBUtil.createEntityManager();
	    //セッションからログイン中の従業員データを取得
	    Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

	    //ページネーション
	    //ページ番号を定義
	    int page;
	    try {
	        //ページ番号のリンクをクリックした時にパラメータからページ番号の値を取得
	        page = Integer.parseInt(request.getParameter("page"));
	    } catch(Exception e) {
	        page = 1;
	    }
	    //「自分の日報の全データを取得する」クエリを実行した結果を格納
	    List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
	                                            .setParameter("employee", login_employee)
	                                            .setFirstResult(15 * (page -1))
	                                            .setMaxResults(15)
	                                            .getResultList();
	    //「自分の日報の全データ数を取得する」クエリを実行した結果を格納
	    long reports_count = (long) em.createNamedQuery("getMyReportsCount", Long.class)
	                                                .setParameter("employee", login_employee)
	                                                .getSingleResult();
	    //エンティティマネージャを終了
	    em.close();

	    //日報の検索結果、データ数、ページ数をJSPに渡す
	    request.setAttribute("reports", reports);
	    request.setAttribute("reports_count", reports_count);
	    request.setAttribute("page", page);

	    if(request.getSession().getAttribute("fliush") != null) {
	        //セッションにフラッシュメッセージがある場合、
	        //セッションから取得したフラッシュメッセージをJSPに渡す
	        request.setAttribute("flush", request.getSession().getAttribute("flush"));
	        //セッションのフラッシュメッセージを削除
	        request.getSession().removeAttribute("flush");
	    }
	    //index.jspに移動
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
	}
}
