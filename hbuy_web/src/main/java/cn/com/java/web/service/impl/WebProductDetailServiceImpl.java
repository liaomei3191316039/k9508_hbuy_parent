package cn.com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.web.model.WebProductDetailEntity;
import cn.com.java.web.service.WebProductDetailService;

/**
 * 
 * @author djin
 *    WebProductDetail业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebProductDetailServiceImpl extends BaseServiceImpl<WebProductDetailEntity> implements WebProductDetailService {
	
}
