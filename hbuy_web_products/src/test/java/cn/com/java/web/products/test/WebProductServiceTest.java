package cn.com.java.web.products.test;

import cn.com.java.model.WebProductDetailEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 *   商品详情的业务层测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebProductServiceTest {

    //日志对象
   // private static final Logger log = Logger.getLogger(AdminMenusServiceTest.class);

    //菜单业务层对象
    @Autowired
    private WebProductDetailService webProductDetailService;

    //测试查询所有商品信息
    @Test
    public void test01(){
        try {
            List<WebProductDetailEntity> list = webProductDetailService.findAll();
            for (WebProductDetailEntity product:list) {
                System.out.println(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试生成商品详情的静态页面
    @Test
    public void test02(){
        webProductDetailService.makeHtmls();
    }

}
