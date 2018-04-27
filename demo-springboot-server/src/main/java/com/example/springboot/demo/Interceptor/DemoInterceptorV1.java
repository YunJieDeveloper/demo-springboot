package com.example.springboot.demo.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/***
 * @Date 2018/3/13
 * @Description  自定义拦截器Demo
 * @author zhanghesheng
 * */
@Slf4j
public class DemoInterceptorV1 implements HandlerInterceptor {

    /**
     * 该方法将在请求处理Controller之前进行调用；
     * 当它返回为false时，表示请求结束，后续的Interceptor 和Controller 都不会再执行；
     * 拦截逻辑一般在此方法中进行。
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        /***
         * 拦截逻辑方法体。
         * */
        // TODO Auto-generated method stub
        log.info("------preHandle------");
        //获取session
        HttpSession session = request.getSession(true);
        //判断用户ID是否存在，不存在就跳转到登录界面
        if(session.getAttribute("userId") == null){
            log.info("------:跳转到login页面！");
            response.sendRedirect(request.getContextPath()+"/login");
            return false;
        }else{
            session.setAttribute("userId", session.getAttribute("userId"));
            return true;
        }

    }


    /**
     * 当前请求进行处理之后，即Controller方法调用之后执行，但是它会在DispatcherServlet进行视图返回渲染之前被调用
     * */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 在DispatcherServlet 渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的
     * */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
