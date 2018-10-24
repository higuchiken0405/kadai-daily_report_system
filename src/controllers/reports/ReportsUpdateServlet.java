package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportsUpdateServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //トークンをパラメータから取得
	    String _token = request.getParameter("_token");
	    if(_token != null && _token.equals(request.getSession().getId())) {
	        //トークンがあり、セッションIDと等しい場合
	        //エンティティマネージャの生成
	        EntityManager em = DBUtil.createEntityManager();
	        //セッションから取得した日報のIDの値で検索した結果を結果に格納
	        Report r = em.find(Report.class, (Integer) request.getSession().getAttribute("report_id"));
	        //日報の日付をパラメータから取得し(String型からDate型に変換して)セット
	        r.setReport_date(Date.valueOf(request.getParameter("report_date")));
	        //タイトル、内容をパラメータから取得しセット
	        r.setTitle(request.getParameter("title"));
	        r.setContent(request.getParameter("content"));
	        //現在日時を更新日時にセット
	        r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

	        //日報をバリデーションした結果のエラーメッセージを取得
	        List<String> errors = ReportValidator.validate(r);
	        if(errors.size() > 0) {
	            //エラーメッセージがある場合
	            //エンティティマネージャを終了
	            em.close();
	            //セッションID、日報データ、エラーメッセージをJSPに渡す
	            request.setAttribute("_token", request.getSession().getId());
	            request.setAttribute("report", r);
	            request.setAttribute("errors", errors);
	            //edit.jspに移動
	            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
	            rd.forward(request, response);
	        } else {
	            //エラーメッセージがない場合
	            //トランザクションを開始
	            em.getTransaction().begin();
	            //トランザクションを確定
	            em.getTransaction().commit();
	            //エンティティマネージャを終了
	            em.close();
	            //セッションにフラッシュメッセージを格納
	            request.getSession().setAttribute("flush", "更新が完了しました。");
	            //セッションから日報のIDを削除
	            request.getSession().removeAttribute("report_id");
	            //日報にインデックスにリダイレクト
	            response.sendRedirect(request.getContextPath() + "/reports/index");
	        }
	    }
	}

}
