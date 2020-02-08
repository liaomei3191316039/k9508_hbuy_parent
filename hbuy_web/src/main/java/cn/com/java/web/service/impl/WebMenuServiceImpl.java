package cn.com.java.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.com.java.web.model.WebMenuEntity;
import cn.com.java.web.service.WebMenuService;

import java.util.List;

/**
 * 
 * @author djin
 *    WebMenu业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebMenuServiceImpl extends BaseServiceImpl<WebMenuEntity> implements WebMenuService {

    //依赖注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //查询所有前台导航菜单
    @Override
    public List<WebMenuEntity> findAll() throws Exception {
        //1.声明前台导航菜单集合
        List<WebMenuEntity> menus = null;
        //2.取到redisTemplate模板中的操作list集合的对象
        ListOperations lop = redisTemplate.opsForList();
        //3.从redis中取到集合数据
        menus = lop.range("menus", 0, -1);
        //4.判断集合有没有数据（是否存在）
        if(menus.size()==0){
            //5.redis中没有此集合，查询数据库
            menus = baseMapper.queryAll();
            //6.将查询出来的数据装入到redis中
            lop.leftPushAll("menus",menus);
            System.out.println("此时走的是mysql数据库。。。。。");
        }else {
            System.out.println("此时走的是redis！！！！");
        }
        return menus;
    }
}
