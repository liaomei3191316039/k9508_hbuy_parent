package cn.com.java.web.sso.service.impl;

import cn.com.java.model.WebUsersEntity;
import cn.com.java.web.service.WebUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author djin
 *    WebUsers业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebUsersServiceImpl extends BaseServiceImpl<WebUsersEntity> implements WebUsersService {

    //注入redis模板
    @Autowired
    private RedisTemplate redisTemplate;

    //完成用户登陆
    @Override
    public String login(WebUsersEntity user) throws Exception{
        //首先根据用户名和密码登陆
        WebUsersEntity loginUser = baseMapper.queryObjectByPramas(user);
        System.out.println(loginUser);
        if(loginUser!=null){
            String token = UUID.randomUUID().toString();
            System.out.println(token);
            //通过redis模板得到操作字符串的对象
            ValueOperations vop = redisTemplate.opsForValue();
            //将用户登陆的数据放在redis中，设置20分钟内有效
            vop.set("sessionid:"+token,loginUser,20, TimeUnit.MINUTES);
            return token;
        }else {
            return "fail";
        }

    }

    //根据登陆的令牌取到redis中的登陆用户数据
    @Override
    public WebUsersEntity getLoginUser(String token) throws Exception {
        WebUsersEntity loginUser = null;
        //判断是否存在此token的用户数据
        if(redisTemplate.hasKey("sessionid:" + token)){
            //创建操作字符串的模板对象
            ValueOperations vop = redisTemplate.opsForValue();
            //取到用户数据
            loginUser = (WebUsersEntity)vop.get("sessionid:" + token);
        }
        return loginUser;
    }

    //一键注销
    @Override
    public void exitUser(String token) throws Exception {
        if(redisTemplate.hasKey("sessionid:" + token)){
            //移除用户数据
            redisTemplate.delete("sessionid:" + token);
        }
    }
}
