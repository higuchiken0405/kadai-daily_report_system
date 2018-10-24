package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class EncryptUtil {

    //パスワードを暗号化するメソッド
    public static String getPasswordEncrypt(String plain_p, String salt) {
        String ret = "";

        if(plain_p != null && !plain_p.equals("")) {
            //パスワードがあり、空行ではない場合
            byte[] bytes;
            //パスワードとソルト(スワードを暗号化する際に付与されるデータ)を結合
            String password = plain_p + salt;
            try {
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
                ret = DatatypeConverter.printHexBinary(bytes);
            } catch(NoSuchAlgorithmException ex) {} //NoSuchAlgorithmException 暗号アルゴリズムでのエラー
        }

        return ret;
    }


}
