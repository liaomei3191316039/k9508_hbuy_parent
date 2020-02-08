package cn.com.java.web.orders.mapper;

import cn.com.java.model.WebOrderEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author djin
 *    WebOrderMapper层
 * @date 2019-12-09 10:27:01
 */
@Repository
public interface WebOrderMapper extends BaseMapper<WebOrderEntity> {

    //监听秒杀来的订单结束过后未支付的
    @Select("SELECT * FROM web_order where endDate <= NOW() and orderStatus = 0 and flag = 2")
    List<WebOrderEntity> listenerOrder() throws Exception;
}
