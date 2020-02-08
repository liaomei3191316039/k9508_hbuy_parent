package cn.com.java.web.seckill.mapper;

import cn.com.java.model.WebSeckillEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author djin
 *    WebSeckillMapper层
 * @date 2019-12-09 10:27:01
 */
@Repository
public interface WebSeckillMapper extends BaseMapper<WebSeckillEntity> {

    //1.设置在秒杀前10分钟内将商品放入到redis中
    @Select("SELECT id,num,productId FROM web_seckill where startTime >= NOW() and startTime <= DATE_ADD(NOW(),INTERVAL 10 MINUTE)")
    List<WebSeckillEntity> selectSecKillByTimes() throws Exception;

    //2.将即将开始的秒杀商品的状态由0(未开始)---->1(已开始)（在查询的同时修改同张表数据会自动上锁）
    @Update("UPDATE web_seckill SET `status`='1' where id in(SELECT * FROM(SELECT id FROM web_seckill where startTime <= DATE_ADD(NOW(),INTERVAL 5 SECOND) and startTime >= DATE_SUB(NOW(),INTERVAL 5 SECOND) and STATUS='0') as secKill)")
    Integer updateUPSecKillStatus() throws Exception;

    //4.将即将开始的秒杀商品的状态由1(已开始)---->2(已结束)
    @Update("UPDATE web_seckill SET `status`='2' where id in(SELECT * FROM(SELECT id FROM web_seckill where endTime <= DATE_ADD(NOW(),INTERVAL 5 SECOND) and endTime >= DATE_SUB(NOW(),INTERVAL 5 SECOND) and STATUS='1') as secKill)")
    Integer updateEndSecKillStatus() throws Exception;



}
