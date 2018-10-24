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

import models.Employee;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportsCreateServlet() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //トークンを取得
	    String _token = request.getParameter("_token");

	    if(_token != null && _token.equals(request.getSession().getId()));
	        //トークンがあり、セッションIDと等しい時
	        //エンティティマネージャを生成
	        EntityManager em = DBUtil.createEntityManager();
	        //日報の日付をコンソールに出力
	        System.out.println(request.getParameter("report_date"));
	        //Reportクラスをインスタンス化
	        Report r = new Report();
	        //ログイン中の従業員データを取得し、セット
	        r.setEmployee((Employee) request.getSession().getAttribute("login_employee"));
	        //現在時刻を取得し、日報の日付に格納
            Date report_date = new Date(System.currentTimeMillis());
            //パラメータから取得した日報の日付を取得
	        String rd_str = request.getParameter("report_date");
	        if(rd_str != null && !rd_str.equals("")) {
	            //パラメータの日付がnull　または　空出ない時
	            //パラメータの日付を格納(String型からDate型へ)
	            report_date = Date.valueOf(request.getParameter("report_date"));
	        }
	        //格納した日付をセット
	        r.setReport_date(report_date);
	        //パラメータからタイトル、内容を取得
	        r.setTitle(request.getParameter("title"));
	        r.setContent(request.getParameter("content"));

	        //現在時刻を取得
	        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
	        r.setCreated_at(currentTime);
            r.setUpdated_at(currentTime);

	        //日報をバリデーションした結果のエラーメッセージリストを取得
	        List<String> errors = ReportValidator.validate(r);
	        if(errors.size() >0) {
	            //エラーメッセージがある場合
	           //エンティティマネージャを終了
	           em.close();
	           //セッションID、Reportインスタンス、エラーメッセージをJSPに渡す
	           request.setAttribute("_token", request.getSession().getId());
	           request.setAttribute("report", r);
	           request.setAttribute("errors", errors);
	           //new.jspに移動
	           RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/report/new.jsp");
	           rd.forward(request, response);
	        } else {
	            //トランザクションの開始
	            em.getTransaction().begin();
	            //DBへ登録
	            em.persist(r);
	            //登録の確定
	            em.getTransaction().commit();
	            //エンティティマネージャを終了
	            em.close();
	            //セッションにフレッシュメッセージを保存
	            request.getSession().setAttribute("flush", "登録が完了しました");
	            //日報一覧へリダイレクト
	            response.sendRedirect(request.getContextPath() + "/reports/index");
	        }

	}

}
