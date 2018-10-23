package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

public class EmployeeValidator {

    //Employeeインスタンスをバリデーションした結果のエラーメッセージのリストを得るメソッド
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag) {
        //エラーメッセージのリストを定義
        List<String> errors = new ArrayList<String>();
        //社員番号をバリデーションした結果のエラーメッセージを取得
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")) {
            //エラーメッセージがある場合、エラーメッセージのリストに追加
            errors.add(code_error);
        }
        //氏名をバリデーションした結果のエラーメッセージを取得
        String name_error = _validateName(e.getName());
        if(!name_error.equals("")) {
            //エラーメッセージがある場合、エラーメッセージのリストに追加
            errors.add(name_error);
        }
        //パスワードをバリデーションした結果のエラーメッセージを取得
        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")) {
            //エラーメッセージがある場合、エラーメッセージのリストに追加
            errors.add(password_error);
        }
        //エラーメッセージのリストを返す
        return errors;
    }
    //社員番号をバリデーションするメソッド
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        if(code == null || code.equals("")) {
            //社員番号がnull　または　空の時、エラーメッセージを返す
            return "社員番号を入力してください。";
        }
        if(code_duplicate_check_flag) {
            //社員番号重複チェックフラグが立っている時
            //エンティティマネージャを生成
            EntityManager em = DBUtil.createEntityManager();
            //「社員番号が登録済みかをチェックする」クエリを実行した結果を格納
            long employees_count = (long) em.createNamedQuery("checkRegisteredCode", Long.class)
                                            //検索に使用する社員番号をセット
                                           .setParameter("code", code)
                                           //１つの結果を得る
                                             .getSingleResult();
            //エンティティマネージャを終了
            em.close();
            if(employees_count > 0) {
                //社員番号が登録済みの場合、エラーメッセージを返す
                return "入力された社員番号の情報は既に存在しています。";
            }
        }

        return "";
    }
    //氏名をバリデーションするメソッド
    private static String _validateName(String name) {
        if(name == null || name.equals("")) {
            //氏名がnull　か　空の時エラーメッセージを返す
            return "氏名を入力してください。";
        }
        //空で返す
        return "";
    }

    //パスワードをバリデーションするメソッド
    private static String _validatePassword(String password, Boolean password_check_flag) {
        if(password_check_flag && (password == null || password.equals(""))) {
            //パスワードチェックフラグが立っていて
            //パスワードがnull　か　空の時エラーパスワードを返す
            return "パスワードを入力してください。";
        }
        //空で返す
        return "";
    }




}
