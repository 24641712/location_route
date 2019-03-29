package util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ly on 2018/4/23.
 */
public class FKSqlSessionFactory {
    private static SqlSessionFactory sqlSessionFactory = null;
    private static final Class CLASS_LOCK = FKSqlSessionFactory.class;//¿‡œﬂ≥ÃÀ¯

    private FKSqlSessionFactory() {
    }

    private static SqlSessionFactory initSqlSessionFactory() {

        SqlSession session = null;

        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            System.out.println(resource + inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (CLASS_LOCK) {
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
        }
        return sqlSessionFactory;
    }

    public static SqlSession opensqlSession(){
        if(sqlSessionFactory == null){
            initSqlSessionFactory();
        }
        return sqlSessionFactory.openSession();
    }
}
