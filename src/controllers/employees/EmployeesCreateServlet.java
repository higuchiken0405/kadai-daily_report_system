package controllers.employees;

import java.io.IOException;
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
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesCreateServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //トークンをパラメータから取得
        String _token = (String)request.getParameter("_token");

        if(_token != null && _token.equals(request.getSession().getId())) {
            //トークンがあり、セッションIDと同じ時
            //エンティティマネージャを生成
            EntityManager em = DBUtil.createEntityManager();
            //Employeeクラスのインスタンス化
	        Employee e = new Employee();
	        //パラメータから取得した社員番号、氏名、パスワード、管理者権限をセット
	        e.setCode(request.getParameter("code"));
	        e.setName(request.getParameter("name"));
	        e.setPassword(
	                EncryptUtil.getPasswordEncrypt(
	                        request.getParameter("password"),
	                        (String)this.getServletContext().getAttribute("salt")
	                        )
	                );
	        e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
	        //現在時刻を取得し、作成日時・更新日時にセット
	        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
	        e.setCreated_at(currentTime);
	        e.setUpdated_at(currentTime);
	        //削除済みフラグを下ろす
	        e.setDelete_flag(0);
	        //Employeeのバリデーションした結果のエラーメッセージを格納
	        List<String> errors = EmployeeValidator.validate(e, true, true);
	        if(errors.size() > 0) {
	            //エラーメッセージがある場合
	            //エンティティマネージャを終了
	            em.close();
	            //セッションID、Employee、エラーメッセージをJSPに渡す
	            request.setAttribute("_token", request.getSession().getId());
	            request.setAttribute("employee", e);
	            request.setAttribute("errors", errors);
	            //new.jspに戻る
	            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
	            rd.forward(request, response);
	        } else {
	            //エラーメッセージがない場合
	            //トランザクション開始
	            em.getTransaction().begin();
	            //DBに登録
	            em.persist(e);
	            //登録とコミットする
	            em.getTransaction().commit();
	            //エンティティマネージャを終了
	            em.close();
	            //セッションにフラッシュメッセージを保存
	            request.getSession().setAttribute("flush", "登録が完了しました。");
	            //indexに移動
	            response.sendRedirect(request.getContextPath() + "/employees/index");
	        }
	    }
	}

}
