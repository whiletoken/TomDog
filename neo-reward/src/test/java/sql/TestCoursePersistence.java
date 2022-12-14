package sql;

import com.spring.sql.HibernateUtil;
import com.tomdog.reward.dto.ReqInfo;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class TestCoursePersistence {

    @Test
    public void testAdd() {

        try {
            // JPA操作对象
            EntityManager entityManager = HibernateUtil.getEntityManger();
            ReqInfo reqInfo = entityManager.find(ReqInfo.class, 3);
            entityManager.close();
            System.out.println(reqInfo);
        } finally {
            HibernateUtil.close();
        }
    }

    @Test
    public void testGet() {
        SessionFactory sessionFactory = new Configuration().configure("META-INF/persistence.xml")
                .buildSessionFactory();
    }

}
