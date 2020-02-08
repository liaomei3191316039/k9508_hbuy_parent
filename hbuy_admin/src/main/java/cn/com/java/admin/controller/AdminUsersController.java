package cn.com.java.admin.controller;

import cn.com.java.admin.utils.MD5;
import cn.com.java.admin.utils.VerifyCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import cn.com.java.admin.model.AdminUsersEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author djin
 *   AdminUsers控制器
 * @date 2019-12-07 11:25:43
 */
@Controller
@RequestMapping("/adminusers")
public class AdminUsersController extends BaseController<AdminUsersEntity>{

	//产生动态验证码
	@RequestMapping("/getVerifyCode")
	public void getVerifyCode(HttpServletResponse response, HttpSession session)throws Exception{
		//产生随机数
		String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
		//将字符串转为小写存放到session容器中
		session.setAttribute("verifyCode",verifyCode.toLowerCase());
		//验证码图片的宽度，高度，图片的输出流对象，验证码字符个数
		VerifyCodeUtils.outputImage(230,35,response.getOutputStream(),verifyCode);
	}

	//进行验证码验证
	@RequestMapping("/userLoginVerify")
	public @ResponseBody
	String userLoginVerify(String verify, HttpSession session){
		//将其转为小写的字符串
		verify = verify.toLowerCase();
		//取出session容器中的验证码
		String verifyCode = (String) session.getAttribute("verifyCode");
		//进行验证码验证
		if(verify.equals(verifyCode)){
			return "success";
		}else {
			return "fail";
		}
	}

	//进行登陆
	@RequestMapping("/login")
	public @ResponseBody
	String login(AdminUsersEntity user, HttpSession session){
		//将用户传来的登陆密码进行MD5加密操作（单向加密的过程），保证用户数据安全
		//单向加密：123123  ----->   4297f44b13955235245b2497399d7a93，此时可以正常转，但是反过来就不行
		String pwd = MD5.md5crypt(user.getPwd());  //将页面传来的密码进行加密做登陆
		user.setPwd(pwd);  //将加密后的密码重新设置到登陆用户对象中
		try {
			AdminUsersEntity loginUser = baseService.findObjectByPramas(user);
			if(loginUser!=null){  //登陆成功
				//将登陆的用户信息存入到session容器中
				session.setAttribute("loginUser",loginUser);
				return "success";
			}else {  //登陆失败
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	//退出登陆
	@RequestMapping("/exit")
	public @ResponseBody
	String exit(HttpSession session){
		try{
			//移除用户登陆的信息
			session.removeAttribute("loginUser");
			return "success";
		}catch (Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
}
