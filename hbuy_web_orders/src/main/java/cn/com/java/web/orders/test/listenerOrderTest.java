package cn.com.java.web.orders.test;

import cn.com.java.web.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class listenerOrderTest {

    @Autowired
    private WebOrderService webOrderService;

    @Scheduled(cron = "0/2 * * * * ? ") // 间隔2秒执行
    public void test01(){
        System.out.println("系统当前时间执行修改状态0---->1"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        try {
            Map<String,Object> map = webOrderService.listenerOrder();
            if((Integer)map.get("code")==404){
                System.out.println("当前没有失效订单");
            }else {
                System.out.println("共找到"+map.get("count")+"条数据");
                List<String> msgs = (List<String>) map.get("msgs");
                for (String msg:msgs) {
                    System.out.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
