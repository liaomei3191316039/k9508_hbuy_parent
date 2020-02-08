package cn.com.java.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.admin.model.AdminMenusEntity;
import cn.com.java.admin.service.AdminMenusService;

/**
 * 
 * @author djin
 *    AdminMenus业务层实现类
 * @date 2019-12-07 11:25:44
 */
@Service
@Transactional
public class AdminMenusServiceImpl extends BaseServiceImpl<AdminMenusEntity> implements AdminMenusService {
	
}
