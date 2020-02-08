package cn.com.java.web.register.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailTest {
	public static void main(String[] args) {
		//SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		//发件方邮箱服务器地址
		email.setHostName("smtp.sina.com");//新浪邮件服务器（用于发、收邮件的）POP3/POP是用来娶邮件的
		//设置发件方邮箱的账号和密码
		email.setAuthentication("djin_19890201", "dj198921410");//账号，密码
		//设置发件方邮箱编码
		email.setCharset("UTF-8");
		try {
			//接收方邮箱账号地址
			email.addTo("502037675@qq.com");
			//发件方邮箱账号地址
			email.setFrom("djin_19890201@sina.com");//必须和Authentication使用的用户相同，否则失败
			//发送邮件的标题
			email.setSubject("haha");      //标题
			//发送邮件的内容
		//	email.setMsg("<h1 style='color:red'>---------测试中邮件发送。。--------</h1>");//内容
			email.setMsg("<img src='http://q1cydzrcd.bkt.clouddn.com/0070bf6daff84d1397b6c52c1386bf5f'/>");
			//发送邮件的方法
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
}
