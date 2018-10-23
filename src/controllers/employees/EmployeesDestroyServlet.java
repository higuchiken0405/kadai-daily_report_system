package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesDestroyServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //パラメータからトークンを取得
	    String _token = request.getParameter("_token");
	    if(_token != null && _token.equals(request.getSession().getId())) {
	        //トークンがあり、セッションIDと等しい場合
	        //エンティティマネージャの生成
	        EntityManager em = DBUtil.createEntityManager();
	        //セッションから取得したEmployeeのIDの値で検索した結果を格納
	        Employee e = em.find(Employee.class, (Integer) request.getSession().getAttribute("employee_id"));

	        //削除済みフラグを立てる
	        e.setDelete_flag(1);
	        //現在日時を取得し、更新日時にセット
	        e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
	        //トランザクションの開始
	        em.getTransaction().begin();
	        //トランザクションのコミット
	        em.getTransaction().commit();
	        //エンティティマネージャの終了
	        em.close();

	        //フラッシュメッセージをセッションに保存
	        request.getSession().setAttribute("flush", "削除が完了しました。");
	        //indexにリダイレクト
	        response.sendRedirect(request.getContextPath() + "/employees/index");
	    }


	}

}
