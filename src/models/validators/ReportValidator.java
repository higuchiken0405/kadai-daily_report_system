package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {

    //レポートをバリーデーションした結果のエラーメッセージのリストを得るメソッド
    public static List<String> validate(Report r) {
        //エラーメッセージのリストを定義
        List<String> errors = new ArrayList<String>();
        //タイトルをバリデーションした結果のエラーメッセージを取得
        String title_error = _validateTitle(r.getTitle());
        if(!title_error.equals("")) {
            //エラーメッセージがある場合、リストに追加
            errors.add(title_error);
        }
        //内容をバリデーションした結果のエラーメッセージを取得
        String content_error = _validateContent(r.getContent());
        if(!content_error.equals("")) {
            //エラーメッセージがある場合、リストに追加
            errors.add(content_error);
        }
        //エラーメッセージのリストを返す
        return errors;
    }

    //タイトルをバリデーションした結果のエラーメッセージを得るメソッド
    private static String _validateTitle(String title) {

        if(title == null || title.equals("")) {
            //タイトルがnull または　空の時、エラーメッセージを返す
            return "タイトルを入力してください";
        }
        //空で返す
        return "";
    }

    //内容をバリデーションした結果のエラーメッセージを得るメソッド
    private static String _validateContent(String content) {
        if(content == null || content.equals("")) {
            //内容がnull または　空の時、エラーメッセージを返す
            return "内容を入力してください";
        }
        //空で返す
        return "";
    }



}
