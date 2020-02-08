package cn.com.java.admin.test;

import cn.com.java.admin.model.AdminMenusEntity;
import cn.com.java.admin.service.AdminMenusService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * 菜单的业务层测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminMenusServiceTest {
    //日志对象
    // private static final Logger log = Logger.getLogger(AdminMenusServiceTest.class);

    //菜单业务层对象
    @Autowired
    private AdminMenusService adminMenusService;

    //测试分页查询
    @Test
    public void test01(){
        try {
            Map<String,Object> map = adminMenusService.findListByPramas(1,2,new AdminMenusEntity());
            System.out.println("总的数据条数："+map.get("count"));
            List<AdminMenusEntity> adminMenusEntityList = (List<AdminMenusEntity>) map.get("data");
            for (AdminMenusEntity menu:adminMenusEntityList) {
                System.out.println(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
