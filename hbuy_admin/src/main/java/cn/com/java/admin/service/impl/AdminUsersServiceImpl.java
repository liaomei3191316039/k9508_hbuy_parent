package cn.com.java.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.admin.model.AdminUsersEntity;
import cn.com.java.admin.service.AdminUsersService;

/**
 * 
 * @author djin
 *    AdminUsers业务层实现类
 * @date 2019-12-07 11:25:43
 */
@Service
@Transactional
public class AdminUsersServiceImpl extends BaseServiceImpl<AdminUsersEntity> implements AdminUsersService {
	
}
