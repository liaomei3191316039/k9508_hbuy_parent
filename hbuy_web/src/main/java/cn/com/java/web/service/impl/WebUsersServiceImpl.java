package cn.com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.web.model.WebUsersEntity;
import cn.com.java.web.service.WebUsersService;

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
