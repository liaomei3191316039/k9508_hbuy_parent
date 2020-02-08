package cn.com.java.web.register.controller;

import cn.com.java.web.register.model.WebUsersEntity;
import cn.com.java.web.register.util.SmsUtil;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author djin
 *   WebUsers控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{

	@RequestMapping("/sendSms")
	public @ResponseBody String sendSms(String phone, String code){
		try {
			return SmsUtil.sendSms(phone,code);
		} catch (ClientException e) {
			e.printStackTrace();
			return "error";
		}
	}
}
