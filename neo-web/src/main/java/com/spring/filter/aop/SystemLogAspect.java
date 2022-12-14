package com.spring.filter.aop;


import bitbucket.neo.util.JacksonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * SystemLogAspect
 * <p>
 * 1、切面（aspect）：类是对物体特征的抽象，切面就是对横切关注点的抽象
 * <p>
 * 2、横切关注点：对哪些方法进行拦截，拦截后怎么处理，这些关注点称之为横切关注点。
 * <p>
 * 3、连接点（joinpoint）：被拦截到的点，因为 Spring 只支持方法类型的连接点，所以在Spring 中连接点指的就是被拦截到的方法，实际上连接点还可以是字段或者构造器。
 * <p>
 * 4、切入点（pointcut）：对连接点进行拦截的定义
 * <p>
 * 5、通知（advice）：所谓通知指的就是指拦截到连接点之后要执行的代码，通知分为前置、后置、异常、最终、环绕通知五类。
 * <p>
 * 6、目标对象：代理的目标对象
 * <p>
 * 7、织入（weave）：将切面应用到目标对象并导致代理对象创建的过程
 * <p>
 * 8、引入（introduction）：在不修改代码的前提下，引入可以在运行期为类动态地添加方法或字段。
 *
 * @author home
 */
@Aspect
public class SystemLogAspect {

    private final static Logger log = LoggerFactory.getLogger(SystemLogAspect.class);

    /**
     * 连接点的集合，连接点：需要增强的方法或类
     * 切入点（pointcut）：对连接点进行拦截的定义
     */
    @Pointcut(value = "@annotation(com.spring.filter.aop.SystemServiceLog)")
    public void pointcut() {

    }

    /**
     * 环绕
     */
    @Around("pointcut()")
    public Object doInvoke(ProceedingJoinPoint pjp) {

        long start = System.currentTimeMillis();

        Object result = null;

        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            throw new RuntimeException(throwable);
        } finally {
            printLog(pjp, result, System.currentTimeMillis() - start);
        }

        return result;
    }

    /**
     * 打印日志
     *
     * @param pjp         连接点
     * @param result      方法调用返回结果
     * @param elapsedTime 方法调用花费时间
     */
    private void printLog(ProceedingJoinPoint pjp, Object result, long elapsedTime) {

        SystemLogStrategy strategy = getFocus(pjp);

        if (null == strategy) {
            return;
        }
        strategy.setThreadId(Long.toHexString(Thread.currentThread().getId()));
        strategy.setResult(JacksonUtils.toJson(result));
        strategy.setElapsedTime(elapsedTime);
        if (strategy.isAsync()) {
            new Thread(() -> log.info(strategy.format(), strategy.args())).start();
        } else {
            log.info(strategy.format(), strategy.args());
        }
    }

    /**
     * 获取注解
     */
    private SystemLogStrategy getFocus(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = pjp.getArgs();
        String targetClassName = pjp.getTarget().getClass().getName();
        try {
            Class<?> clazz = Class.forName(targetClassName);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {

                if (!methodName.equals(method.getName()) || args.length != method.getParameterCount()) {
                    continue;
                }

                SystemLogStrategy strategy = new SystemLogStrategy();
                strategy.setClassName(className);
                strategy.setMethodName(methodName);

                SystemServiceLog systemServiceLog = method.getAnnotation(SystemServiceLog.class);
                if (null != systemServiceLog) {
                    strategy.setArguments(JacksonUtils.toJson(args));
                    strategy.setDescription(systemServiceLog.description());
                    strategy.setAsync(systemServiceLog.async());
                    return strategy;
                }

                return null;
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
