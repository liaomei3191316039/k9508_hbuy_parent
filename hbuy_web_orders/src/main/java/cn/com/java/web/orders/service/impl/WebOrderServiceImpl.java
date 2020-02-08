package cn.com.java.web.orders.service.impl;

import cn.com.java.model.WebOrderEntity;
import cn.com.java.model.WebUsersEntity;
import cn.com.java.web.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author djin
 *    WebOrder业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebOrderServiceImpl extends BaseServiceImpl<WebOrderEntity> implements WebOrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String,Object> listenerOrder() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",404);
        //查询以及失效的订单
        List<WebOrderEntity> orderses = webOrderMapper.listenerOrder();
        if(orderses!=null){
            map.put("code",200);
            map.put("count",orderses.size());
            //操作成功与否的提示
            List<String> msgs = new ArrayList<>();
            //通过循环将订单状态修改，然后在redis中修改数据
            for (WebOrderEntity order:orderses) {
                WebOrderEntity updOrderPra = new WebOrderEntity();
                updOrderPra.setId(order.getId());
                updOrderPra.setOrderstatus("5");
                //做mysql数据库的订单状态修改
                Integer count = baseMapper.update(updOrderPra);
                if(count>0){
                    //操作redis
                    SetOperations sop = redisTemplate.opsForSet();
                    String key = "secKill_"+order.getSecId()+"_"+order.getProIds();
                    String value = order.getSecId()+","+order.getUserid()+","+order.getProIds();
                    Long reCount = 0l;
                    if(sop.isMember(key,value)){  //判断存在
                        reCount = sop.remove(key, value);  //删除已存在的秒杀中的数据
                    }
                    ListOperations lop = redisTemplate.opsForList();
                    Long addCount = lop.leftPush(order.getSecId() + "_" + order.getProIds(), order.getSecId() + "_" + order.getProIds());
                    if(reCount>0&&addCount>0){
                        msgs.add(order.getSecId() + "_" + order.getProIds()+"已加回去");
                    }
                }
            }
            map.put("msgs",msgs);  //加入操作的提示
        }
        return map;
    }

    //加载登陆用户的订单数据
    @Override
    public List<WebOrderEntity> findOrdersByUid(String token) throws Exception {
        List<WebOrderEntity> webOrderEntities = null;
        ValueOperations vop = redisTemplate.opsForValue();
        WebUsersEntity loginUser = (WebUsersEntity)vop.get("sessionid:" + token);
        if(loginUser!=null){
            WebOrderEntity ordersPra = new WebOrderEntity();
            ordersPra.setUserid(loginUser.getId());
            webOrderEntities = baseMapper.queryManyByPramas(ordersPra);
        }
        return webOrderEntities;
    }


}
