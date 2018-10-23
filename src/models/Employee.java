package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
//エンティティクラス
@Entity
//名前付きクエリ
@NamedQueries({
    @NamedQuery(
            //従業員の全てのデータを取得
            name = "getAllEmployees",
            query = "SELECT e FROM Employee AS e ORDER BY e.id DESC"
            ),
    @NamedQuery(
            //従業員のデータ数を取得
            name = "getEmployeesCount",
            query = "SELECT COUNT(e) FROM Employee AS e"
            ),
    @NamedQuery(
            //社員番号が登録済みかチェック
            name = "checkRegisteredCode",
            query = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :code"
            ),
    @NamedQuery(
            //ログイン社員番号とパスワードをチェック
            name = "checkLoginCodeAndPassword",
            query = "SELECT e FROM Employee AS e WHERE e.delete_flag = 0 AND e.code = :code AND e.password = :pass"
            )
})
//テーブル名
@Table(name = "employees")
public class Employee {

    //プライマリキー
    @Id
    //IDカラム設定　名前id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //社員番号カラム設定　名前code NOT NULL 一意制約
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    //氏名カラム設定　名前name NOT NULL
    @Column(name = "name", nullable = false)
    private String name;
    //パスワードカラム設定　名前password 文字数64(SAH256による暗号化のため) NOT NULL
    @Column(name = "password", length = 64, nullable = false)
    private String password;
    //管理者権限カラム設定　名前admin_flag NOT NULL
    @Column(name = "admin_flag", nullable = false)
    private Integer admin_flag;
    //作成日時カラム設定　名前created_at NOT NULL
    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;
    //更新日時カラム設定　名前updated_at NOT NULL
    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;
    //削除済みフラグカムラ設定　名前delete_flag NOT NULL
    @Column(name = "delete_flag", nullable = false)
    private Integer delete_flag;


    //ゲッター・セッター
    //ID
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    //社員番号
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    //氏名
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //パスワード
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    //管理者権限
    public Integer getAdmin_flag() {
        return admin_flag;
    }
    public void setAdmin_flag(Integer admin_flag) {
        this.admin_flag = admin_flag;
    }
    //作成日時
    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    //更新日時
    public Timestamp getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
    //削除済みフラグ
    public Integer getDelete_flag() {
        return delete_flag;
    }
    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }
}



