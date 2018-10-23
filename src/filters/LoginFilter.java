package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

@WebFilter("/*")
public class LoginFilter implements Filter {

    public LoginFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

	    //コンテキストパスに取得(requestをServletRequestからHttpServletRequestにキャスト)
	    String context_path = ((HttpServletRequest)request).getContextPath();
	    //サーブレットパスの取得(requestをServletRequestからHttpServletRequestにキャスト)
        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")) {
            //cssファイル以外にアクセスした時(cssへのアクセス時にログイン状態を確認しないようにするため)
            //セッション開始か既存のセッションの取得
            HttpSession session = ((HttpServletRequest)request).getSession();
            //ログイン中のEmployeeを取得
            Employee e = (Employee) session.getAttribute("login_employee");

            if(!servlet_path.equals("/login")) {
                //ログインページ以外にアクセスした時
                if(e == null) {
                    //ログイン状態でない場合、トップページに強制リダイレクト
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }

                if(servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {
                    //従業員管理(/employees)のページにアクセスして、管理者権限が一般(admin_flag=0)の時
                    //トップページに強制リダイレクト
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    //処理終了
                    return;
                }
            }  else {
                //ログインページにアクセスした時
                if(e != null) {
                    //ログイン状態ならトップページに強制リダイレクト
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    //処理終了
                    return;
                }
            }

        }

	    chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
