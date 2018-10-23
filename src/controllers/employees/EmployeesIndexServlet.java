package controllers.employees;

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
import utils.DBUtil;

@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeesIndexServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //エンティティマネージャの生成
        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        //初期ページの設定
        int page = 1;
        try{
            //ページ番号をクリックして来た場合、パラメータからページ番号を取得
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }
        //「全ての従業員のデータを呼び出す」クエリを実行した結果を格納
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
                                     .setFirstResult(15 * (page - 1)) //開始位置を指定
                                     .setMaxResults(15) //取得する結果の最大値を指定
                                     .getResultList(); //複数の結果を取得
        //「従業員のデータの数を呼び出す」クエリを実行した結果を格納j
        long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class)
                                       .getSingleResult(); //１つの結果を格納
        //エンティティマネージャを終了
        em.close();

        //JSPに、従業員全データ・データ数・ページ番号を渡す
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            //セッションにフラッシュメッセージがある場合、JSPに渡す。
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            //セッションのフラッシュメッセージを削除
            request.getSession().removeAttribute("flush");
        }
        //index.jspに移動
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }
}
