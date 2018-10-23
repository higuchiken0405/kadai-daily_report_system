package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {

    //永続的ユニット名の設定
    private static final String PERSISTENCE_UNIT_NAME = "daily_report_system";
    //エンティティマネージャファクトリを定義
    private static EntityManagerFactory emf;

    //エンティティマネージャを生成するメソッド
    public static EntityManager createEntityManager() {
        //エンティティマネージャを返す
        return _getEntityManagerFactory().createEntityManager();
    }
    //エンティティマネージャファクトリを生成するメソッド
    private static EntityManagerFactory _getEntityManagerFactory() {
        if(emf == null) {
            //エンティティマネージャがない場合、エンティティマネージャファクトリを生成
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        //エンティティマネージャファクトリを返す
        return emf;
    }
}
