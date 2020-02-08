package cn.com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.web.model.WebBannerEntity;
import cn.com.java.web.service.WebBannerService;

/**
 * 
 * @author djin
 *    WebBanner业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebBannerServiceImpl extends BaseServiceImpl<WebBannerEntity> implements WebBannerService {
	
}
