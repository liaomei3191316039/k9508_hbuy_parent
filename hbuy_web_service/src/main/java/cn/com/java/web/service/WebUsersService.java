package cn.com.java.web.service;

import cn.com.java.model.WebUsersEntity;

/**
 * 
 * @author djin
 *    WebUsers业务层接口
 * @date 2019-12-09 10:27:01
 */
public interface WebUsersService extends BaseService<WebUsersEntity>{

    //完成用户登陆
    String login(WebUsersEntity user) throws Exception;

    //根据登陆的令牌取到redis中的登陆用户数据
    WebUsersEntity getLoginUser(String token) throws Exception;

    //一键注销
    void exitUser(String token) throws Exception;

}
