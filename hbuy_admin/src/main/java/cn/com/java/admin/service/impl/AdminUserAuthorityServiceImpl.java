package cn.com.java.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.admin.model.AdminUserAuthorityEntity;
import cn.com.java.admin.service.AdminUserAuthorityService;

/**
 * 
 * @author djin
 *    AdminUserAuthority业务层实现类
 * @date 2019-12-07 11:25:44
 */
@Service
@Transactional
public class AdminUserAuthorityServiceImpl extends BaseServiceImpl<AdminUserAuthorityEntity> implements AdminUserAuthorityService {
	
}
