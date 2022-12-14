package com.spring.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateUtil {

    public static EntityManagerFactory emf = createEntityManagerFactory();
    public static ThreadLocal<EntityManager> local = new ThreadLocal<>();

    //1.获得实体管理工厂
    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("mysql-jpa");
    }

    public static EntityManager getEntityManger() {
        if (local.get() != null) {
            return local.get();
        }
        local.set(emf.createEntityManager());
        return local.get();
    }

    public static void close() {
        if (local.get() != null) {
            local.get().close();
            local.remove();
        }
    }

}
