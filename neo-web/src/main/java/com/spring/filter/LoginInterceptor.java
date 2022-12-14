package com.spring.filter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginInterceptor
 *
 * @author willian
 * @date 2019-12-04 12:42
 **/

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在执行handler之前来执行的
     * 用于用户认证校验、用户权限校验
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        return true;
    }

    /**
     * 在执行handler返回modelAndView之前来执行
     * 如果需要向页面提供一些公用 的数据或配置一些视图信息，使用此方法实现 从modelAndView入手
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {


    }

    /**
     * 执行handler之后执行此方法
     * 作系统 统一异常处理，进行方法执行性能监控，在preHandle中设置一个时间点，
     * 在afterCompletion设置一个时间，两个时间点的差就是执行时长
     * 实现 系统 统一日志记录
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {


    }

}
