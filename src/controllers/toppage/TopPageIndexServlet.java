package controllers.toppage;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TopPageIndexServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
