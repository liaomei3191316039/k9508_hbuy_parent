package cn.com.java.web.seckill.service.impl;

import cn.com.java.web.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    //注入RabbitMQ模板对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //将要提交的订单数据加入到RabbitMQ中
    @Override
    public void addRabbitMQToExC(Long secId, Long proId,Float secPrice, Long uid) throws Exception {
         //设置装入的数据
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("secId",secId);
        dataMap.put("proId",proId);
        dataMap.put("secPrice",secPrice);
        dataMap.put("uid",uid);
        rabbitTemplate.convertAndSend("ex_djin",null,dataMap);
    }

    @Override
    public void addRabbitMQToBuyCar(String proIds, Double allPrice, Long uid) throws Exception {

    }
}
