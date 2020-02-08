package cn.com.java.web.sso.controller;

import cn.com.java.model.WebUsersEntity;
import cn.com.java.web.sso.utils.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author djin
 *   WebUsers控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{
	
	//用户登陆
	@RequestMapping("/loginUser")
	public @ResponseBody String loginUser(WebUsersEntity user, HttpServletResponse response){
		//进行密码加密
		user.setPwd(MD5.md5crypt(user.getPwd()));
        //调用业务层
		try {
			String token = webUsersService.login(user);
			if(!"fail".equals(token)){
				//构建cookie
				Cookie cookie = new Cookie("token",token);
				//设置有效时间
				cookie.setMaxAge(20*60);
				//设置产生cookie的服务器的路径
				cookie.setPath("/webusers");
				//将产生的cookie响应回客户端
				response.addCookie(cookie);
                return token;
			}else {
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}

	//取到令牌token
	@RequestMapping("/getToken")
	public @ResponseBody String getToken(HttpServletRequest request){
		//获取请求中的所有cookie
		Cookie[] cookies = request.getCookies();
		//定义令牌为空字符串
		String token = "";
		//判断整个cookie数组不为空
		if(cookies!=null){
			//声明存有令牌的cookie
			Cookie tokenCookie = null;
			//通过循环找到某一个cookie
			for (Cookie cookie:cookies) {
				//判断cookie的名字为token,则找到存放该令牌的cookie
				if("token".equals(cookie.getName())){
					tokenCookie = cookie;
					break;
				}
			}
			//判断存储令牌的cookie是否为空
			if(tokenCookie!=null){
				//取出令牌
				token = tokenCookie.getValue();
			}
		}
		return token;
	}

	//根据令牌获取redis中的用户数据
	@RequestMapping("/getLoginUser")
	public @ResponseBody WebUsersEntity getLoginUser(String token){
		try {
			return webUsersService.getLoginUser(token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//一键注销
	@RequestMapping("/exitUser")
	public @ResponseBody String exitUser(String token,HttpServletRequest request,HttpServletResponse response){
		//获取请求中的所有cookie
		Cookie[] cookies = request.getCookies();
		//定义存有令牌的cookie
		Cookie tokenCookie = null;
		if(cookies!=null){
			for (Cookie cookie:cookies) {
				//通过token找其cookie对象
				if(token.equals(cookie.getValue())){
					tokenCookie = cookie;  //找到了
					break;
				}
			}
		}
		if(tokenCookie!=null){//令牌的cookie存在,则移除
			tokenCookie = new Cookie("token","");
			tokenCookie.setPath("/webusers");
			tokenCookie.setMaxAge(0);  //失效
			response.addCookie(tokenCookie);
		}
		try {
			webUsersService.exitUser(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
