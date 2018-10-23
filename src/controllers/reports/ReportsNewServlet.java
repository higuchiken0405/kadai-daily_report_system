package controllers.reports;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;

@WebServlet("/reports/new")
public class ReportsNewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportsNewServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //セッションIDをJSPに渡す
	    request.setAttribute("_token", request.getSession().getId());

	    //Reportクラスをインスタンス化
	    Report r = new Report();
	    //現在時刻を取得し、日報の日付にセット
	    r.setReport_date(new Date(System.currentTimeMillis()));
	    //ReportインスタンスをJSPに送る
	    request.setAttribute("report", r);
	    //new.jspへ移動
	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
	    rd.forward(request, response);
	}
}
