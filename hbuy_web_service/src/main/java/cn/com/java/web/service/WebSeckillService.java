package cn.com.java.web.service;

import cn.com.java.model.WebSeckillEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author djin
 *    WebSeckill业务层接口
 * @date 2019-12-09 10:27:01
 */
public interface WebSeckillService extends BaseService<WebSeckillEntity>{

    //1.将秒杀数据放入到redis中
    Map<String,Object> addSecKillToRedis() throws Exception;

    //2.将即将开始的秒杀商品的状态由0(未开始)---->1(已开始)
    Integer updateUPSecKillStatus() throws Exception;

    //3.开始进行秒杀
    Map<String,Object> doSecKill(Long secId, String token) throws Exception;

    //4.将即将开始的秒杀商品的状态由1(已开始)---->2(已结束)
    Integer updateEndSecKillStatus() throws Exception;

    //模拟登陆
   // Map<String,Object> login(Long uid) throws Exception;

    //加载要秒杀的商品信息
    List<Map<String,Object>> findUPSecKill() throws Exception;
	
}
