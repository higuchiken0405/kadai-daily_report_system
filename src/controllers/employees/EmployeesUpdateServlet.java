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

@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesUpdateServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //パラメータからトークンを取得
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            //トークンがあり、セッションIDを等しい場合
            //エンティティマネージャの生成
            EntityManager em = DBUtil.createEntityManager();
            //セッションから取得したIDの値で検索した結果を格納
            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));

            //社員番号重複チェックフラグを立てる
            Boolean code_duplicate_check = true;
            if(e.getCode().equals(request.getParameter("code"))) {
                //社員番号が重複する場合
                //パラメータの社員番号をセットしない
                //社員番号重複チェックフラグを下ろす
                code_duplicate_check = false;
            } else {
                //社員番号が重複しない場合
                //パラメータの社員番号をセット
                e.setCode(request.getParameter("code"));
            }
            //パスワードチェックフラグを立てる
            Boolean password_check_flag = true;
            //パラメータからパスワードを取得
            String password = request.getParameter("password");
            if(password == null || password.equals("")) {
                //パスワードが無かったり、空だった場合
                //セットせずに、パスワードチェックフラグを下ろす
                password_check_flag = false;
            } else {
                //パスワードがある場合
                //パスワードをセット
                e.setPassword(
                        EncryptUtil.getPasswordEncrypt(
                                password,
                                (String)this.getServletContext().getAttribute("salt")
                                )
                        );
            }
            //氏名、管理者権限をパラメータから取得してセット
            e.setName(request.getParameter("name"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
            //現在時刻を取得し、更新時刻にセット
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            //削除済みフラグを下ろす
            e.setDelete_flag(0);

            //Employeeをバリデーションした結果のエラーメッセージを格納
            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);
            if(errors.size() > 0) {
                //エラーメッセージがある場合
                //エンティティマネージャの生成
                em.close();
                //セッションID、Employee、エラーメッセージをJSPに渡す
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);
                //edit.jspに戻る
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
                rd.forward(request, response);
            } else {
                //エラーメッセージがない場合
                //トランザクションの開始
                em.getTransaction().begin();
                //トランザクションのコミット
                em.getTransaction().commit();
                //エンティティマネージャを終了
                em.close();
                //フラッシュメッセージをセッションに保存
                request.getSession().setAttribute("flush", "更新が完了しました。");
                //セッションのEmployeeのIDを削除
                request.getSession().removeAttribute("employee_id");
                //indexへリダイレクト
                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }
}
