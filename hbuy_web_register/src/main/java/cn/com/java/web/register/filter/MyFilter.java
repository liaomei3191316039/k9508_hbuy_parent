package cn.com.java.web.register.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *   自定义的过滤器
 *      修改所有请求中的字符的编码
 */
@WebFilter(urlPatterns = "/*")  //表示所有的请求均要通过此过滤器
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器的初始化init方法被执行了。。。");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      //  System.out.println("过滤器的核心doFilter方法被执行了。。。");
        //得到HTTP请求响应对象
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //设置请求响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //设置响应正文的字符编码
        response.setContentType("text/html;charset=UTF-8");
        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("过滤器的销毁destroy方法被执行了。。。");
    }
}
