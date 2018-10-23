package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //セッションID、エラーフラグ(を下ろす)をJSPに渡す
	    request.setAttribute("_token", request.getSession().getId());
	    request.setAttribute("hasError", false);
	    if(request.getSession().getAttribute("flush") != null) {
	        //フラッシュメッセージがセッションにある場合
	        //セッションからフラッシュメッセージを取得し、JSPに渡す
	        request.setAttribute("flush", request.getSession().getAttribute("flush"));
	        //セッションからフラッシュメッセージを削除
	        request.getSession().removeAttribute("flush");
	    }
	    //login.jspに移動
	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
	    rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //チェック結果を定義(初期値false)
	    Boolean check_result = false;

	    //パラメータから社員番号とパスワードを取得
	    String code = request.getParameter("code");
	    String plain_pass = request.getParameter("password");
	    //Employeeインスタンスを定義(初期値null)
	    Employee e = null;
	    if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
	        //社員番号があり、plain_passがnull出ない　かつ　空でもない
	        //エンティティマネージャを生成
	        EntityManager em = DBUtil.createEntityManager();
	        //パスワードを暗号化
	        String password = EncryptUtil.getPasswordEncrypt(
	                plain_pass,
	                (String) this.getServletContext().getAttribute("salt")
	                );
	        try {
	            //「ログインの社員番号とパスワードをチャックする」クエリを実行した結果を格納
	            e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
	                    //実行に使用する値をセット
	                    .setParameter("code", code)
	                    .setParameter("pass", password)
	                    //１つの結果を取得
	                    .getSingleResult();
	        } catch(NoResultException ex) {}

	        //エンティティマネージャを生成
	        em.close();

	        if(e != null) {
	            //社員番号とパスワードが一致する結果があれば、
	            //チェック結果をtrue
	            check_result = true;
	        }
	    }

	    if(!check_result) {
	        //チェック結果がfalseの場合
	        //セッションID、エラーフラグ(を立てる)、社員番号をJSPに渡す
	        request.setAttribute("_token", request.getSession().getId());
	        request.setAttribute("hasError", true);
	        request.setAttribute("code", code);
	        //login.jspに移動
	        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
	        rd.forward(request, response);
	    } else {

	        //チェック結果がtrueの場合
	        //セッションに社員番号とパスワードが一致するEmployeeを保存
	        request.getSession().setAttribute("login_employee", e);
	        //フラッシュメッセージをセッションに保存
	        request.getSession().setAttribute("flush", "ログインしました");
	        //トップページに移動
	        response.sendRedirect(request.getContextPath() + "/");
	    }
	}

}
