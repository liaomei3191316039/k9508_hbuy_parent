package cn.com.java.web.orders.controller;

import cn.com.java.model.WebOrderEntity;
import cn.com.java.web.orders.utils.OrderUtils;
import cn.com.java.web.service.WebOrderService;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import com.rabbitmq.client.Channel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author djin
 *   WebOrder控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/weborder")
public class WebOrderController extends BaseController<WebOrderEntity>{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebOrderService webOrderService;

    /**
     *    消息队列将请求发送给订单支付模块
     * @param dataMap  发送的数据，从MQ中取出
     * @param channel  执行的通道
     * @param headers  执行响应回的结果
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value = "qu_djin"),  //队列的名字
            exchange = @Exchange(value = "ex_djin",type = "topic")  //交换机的名字,交换机的类型
    ))  //配置发送数据的交换机和队列的基本信息
    @RabbitHandler   //配置自动发送数据到订单模块
    public void sendOrdersData(@Payload Map<String,Object> dataMap, Channel channel, @Headers Map<String,Object> headers){
        try {
            //1.从配置中的交换机的队列中取出数据
            Long secId = (Long) dataMap.get("secId");  //秒杀的id
            Long uid = (Long) dataMap.get("uid");  //用户id
            Long proId = (Long) dataMap.get("proId");
            Float secPrice = (Float) dataMap.get("secPrice");
            //2、安全监测
            System.out.println("OrderController.....安全监测");
            //3、创建订单
            //3.1、获取订单编号  16位数字字符串
            WebOrderEntity orders = new WebOrderEntity();
            //获取订单编号
            orders.setOrderno(OrderUtils.generateOrderNo(uid));
            //设置订单的用户id
            orders.setUserid(Long.parseLong(uid.toString()));
            //设置订单状态
            orders.setOrderstatus("0");
            //订单总价
            orders.setCost(secPrice);
            //设置秒杀id
            orders.setSecId(secId);
            //设置商品id
            orders.setProIds(proId.toString());
            //设置订单来源
            orders.setFlag(2);
            //设置创建时间
            orders.setCreateDate(new Date());
            //设置结束时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE,10);
            Date endDate = calendar.getTime();
            orders.setEndDate(endDate);
            //执行添加订单,mysql数据库的添加
            webOrderService.save(orders);
            //请求的线程暂停3秒钟
            Thread.sleep(3000);
            //4、支付订单，接入支付接口（支付宝，微信，网银。。。）
            System.out.println(orders.getOrderno()+"的订单已加入到mysql中");
            //5、手动确认订单处理完毕的数据条数
            long endTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            System.out.println("endTag"+endTag);
            //MQ的通道发送请求数据执行的结果改为手动的
            channel.basicAck(endTag,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *    消息队列将请求发送给订单支付模块
     * @param dataMap  发送的数据，从MQ中取出
     * @param channel  执行的通道
     * @param headers  执行响应回的结果
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value = "qu_buyCar"),  //队列的名字
            exchange = @Exchange(value = "ex_buyCar",type = "topic")  //交换机的名字,交换机的类型
    ))  //配置发送数据的交换机和队列的基本信息
    @RabbitHandler   //配置自动发送数据到订单模块
    public void sendOrdersDataByBuyCar(@Payload Map<String,Object> dataMap, Channel channel, @Headers Map<String,Object> headers){
        try {
            //1.从配置中的交换机的队列中取出数据
            Long uid = (Long) dataMap.get("uid");  //用户id
            String proIds = (String) dataMap.get("proIds");
            Float allPrice = (Float) dataMap.get("allPrice");
            //2、安全监测
            System.out.println("OrderController.....安全监测");
            //3、创建订单
            //3.1、获取订单编号  16位数字字符串
            WebOrderEntity orders = new WebOrderEntity();
            //获取订单编号
            orders.setOrderno(OrderUtils.generateOrderNo(uid));
            //设置订单的用户id
            orders.setUserid(Long.parseLong(uid.toString()));
            //设置订单状态
            orders.setOrderstatus("0");
            //订单总价
            orders.setCost(allPrice);
            //设置秒杀id
            orders.setSecId(null);
            //设置商品id
            orders.setProIds(proIds);
            //设置订单来源
            orders.setFlag(1);
            //设置创建时间
            orders.setCreateDate(new Date());
            //设置结束时间
            orders.setEndDate(null);
            //执行添加订单,mysql数据库的添加
            webOrderService.save(orders);
            //请求的线程暂停3秒钟
            Thread.sleep(3000);
            //4、支付订单，接入支付接口（支付宝，微信，网银。。。）
            System.out.println(orders.getOrderno()+"的订单已加入到mysql中");
            //5、手动确认订单处理完毕的数据条数
            long endTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            System.out.println("endTag"+endTag);
            //MQ的通道发送请求数据执行的结果改为手动的
            channel.basicAck(endTag,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //根据条件查询订单数据
    @RequestMapping("/loadManyOrders")
    public @ResponseBody List<WebOrderEntity> loadManyOrders(String token){
        try {
            return webOrderService.findOrdersByUid(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
