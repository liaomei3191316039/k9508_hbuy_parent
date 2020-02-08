package cn.com.java.web.register.service.impl;

import cn.com.java.web.register.model.WebUsersEntity;
import cn.com.java.web.register.service.WebUsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author djin
 *    WebUsers业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebUsersServiceImpl extends BaseServiceImpl<WebUsersEntity> implements WebUsersService {
	
}
