package controllers.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //セッションからログインしているEmployeeを削除
	    request.getSession().removeAttribute("login_employee");
	    //セッションにフラッシュメッセージを保存
	    request.getSession().setAttribute("flush", "ログアウトしました。");
	    //ログインページに移動
	    response.sendRedirect(request.getContextPath() + "/login");


	}


}
