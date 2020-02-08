package cn.com.java.admin.interceptor;

import cn.com.java.admin.model.AdminUsersEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *   自定义的拦截器
 *     作用：登陆---->首页
 */
public class MyInterceptor implements HandlerInterceptor {

    //拦截器的核心方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println("-------preHandle()被执行了。。。。。。");
        //根据request请求对象得到session容器
        HttpSession session = request.getSession();
        //从session中取到登陆的用户
        AdminUsersEntity loginUser = (AdminUsersEntity) session.getAttribute("loginUser");
        if(loginUser!=null){  //登陆
            return true;  //放行，继续执行其他的拦截器
        }else {   //未登陆
            request.setAttribute("loginUIMsg","loginUIMsg");  //存提示
            request.getRequestDispatcher("/").forward(request,response);  //转发到登陆页面
            return false;  //阻止请求继续向下执行，将请求拦截下来
        }

    }

    //拦截POST请求要执行的方法
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("-------postHandle()被执行了。。。。。。");
    }

    //拦截之后的要执行的方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        System.out.println("-------afterCompletion()被执行了。。。。。。");
    }
}
