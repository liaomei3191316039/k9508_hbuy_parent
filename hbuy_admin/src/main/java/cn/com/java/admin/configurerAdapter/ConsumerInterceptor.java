package cn.com.java.admin.configurerAdapter;

import cn.com.java.admin.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * @author: djin
 * @date: 2018/10/16
 * Description:配置URLInterceptor拦截器，以及拦截路径
 *               引用自定义的拦截器
 * @Configuration  实例化此拦截器，读取其配置
 */
@Configuration
public class ConsumerInterceptor extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("执行了拦截器的配置工具类。。。");
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns(Arrays.asList("/model/**","/html/**","/adminmenus/**","/adminuserauthority/**","/adminusers/exit"));
        super.addInterceptors(registry);  //配置系统默认的拦截器
    }

}
