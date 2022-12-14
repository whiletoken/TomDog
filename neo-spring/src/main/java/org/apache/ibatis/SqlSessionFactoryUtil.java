package org.apache.ibatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MyBatis 测试工具
 * getOpenSession 获得SqlSession
 * getTestMapper 传入dao接口，返回dao实现类
 */
@Slf4j
public class SqlSessionFactoryUtil {

    private static SqlSessionFactory sqlSessionFactory = null;

    private static final ThreadLocal<SqlSession> sqlSessionThreadLocal = new ThreadLocal<>();

    public synchronized static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (SqlSessionFactoryUtil.sqlSessionFactory == null) {
            SqlSessionFactoryUtil.sqlSessionFactory = sqlSessionFactory;
        }
    }

    public static SqlSession getOpenSession() {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            sqlSessionThreadLocal.set(session);
        } catch (Exception e) {
            log.error("exception is", e);
        }
        return session;
    }

    public static void closeSession() {
        if (sqlSessionThreadLocal.get() != null) {
            sqlSessionThreadLocal.get().close();
            sqlSessionThreadLocal.remove();
        }
    }

    public static void rollback() {
        if (sqlSessionThreadLocal.get() != null) {
            sqlSessionThreadLocal.get().rollback();
        }
    }

}
