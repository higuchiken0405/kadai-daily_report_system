package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery (
            //日報の全データを取得
            name="getAllReports",
            query="SELECT r FROM Report AS r ORDER BY r.id DESC"
            ),
    @NamedQuery (
            //日報の全データ数を取得
            name="getReportsCount",
            query="SELECT COUNT(r) FROM Report AS r"
            ),
    @NamedQuery (
            //自分の日報の全データ
            name="getMyAllReports",
            query="SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"
            ),
    @NamedQuery (
            //自分の日報の全データ数を取得
            name="getMyReportsCount",
            query="SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"
            ),
})
@Table(name = "reports")


public class Report {
    //プライマリキー
    @Id
    //カラム設定　名前id
    @Column(name = "id")
    //AUTO INCREMENT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //1対多　report側が多
    @ManyToOne
    //カラム設定　名前employee_id NOT NULL
    @JoinColumn(name = "employee_id", nullable=false)
    private Employee employee;

    //カラム設定　名前report_date NOT NULL
    @Column(name = "report_date", nullable = false)
    //Dateは日時のみ(Timestampはミリ秒まで)
    private Date report_date;

    //カラム設定　名前title 文字数255 NOT NULL
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    // テキストエリア指定(改行も反映される)
    @Lob
    //カラム設定　名前content NOT NULL
    @Column(name = "content", nullable = false)
    private String content;

    //カラム設定　名前created_at NOT NULL
    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    //カラム設定　名前updated_at NOT NULL
    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    //ゲッター・セッター
    //id
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    //employee
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    //report_date
    public Date getReport_date() {
        return report_date;
    }
    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }
    //title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    //content
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    //created_at
    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    //updated_at
    public Timestamp getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
