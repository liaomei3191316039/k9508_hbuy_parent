package cn.com.java.web.buycar.service.impl;

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

    @Override
    public void addRabbitMQToExC(Long secId, Long proId, Float secPrice, Long uid) throws Exception {

    }

    //将要提交的订单数据加入到RabbitMQ中
    @Override
    public void addRabbitMQToBuyCar(String proIds,Float allPrice, Long uid) throws Exception {
         //设置装入的数据
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("proIds",proIds);
        dataMap.put("allPrice",allPrice);
        dataMap.put("uid",uid);
        rabbitTemplate.convertAndSend("ex_buyCar",null,dataMap);
    }
}
