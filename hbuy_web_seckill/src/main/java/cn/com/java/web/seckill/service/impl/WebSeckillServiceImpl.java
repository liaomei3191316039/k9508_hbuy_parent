package cn.com.java.web.seckill.service.impl;

import cn.com.java.model.WebProductDetailEntity;
import cn.com.java.model.WebSeckillEntity;
import cn.com.java.model.WebUsersEntity;
import cn.com.java.web.service.RabbitMQService;
import cn.com.java.web.service.WebSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author djin
 *    WebSeckill业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebSeckillServiceImpl extends BaseServiceImpl<WebSeckillEntity> implements WebSeckillService {

    //引入redis
    @Autowired
    private RedisTemplate redisTemplate;

    //引入RabbitMQ业务层对象
    @Autowired
    private RabbitMQService rabbitMQService;

    //将秒杀数据放入到redis中
    @Scheduled(cron = "0/5 * * * * ? ") // 间隔5秒执行
    @Override
    public Map<String, Object> addSecKillToRedis() throws Exception {
        System.out.println("系统当前时间执行添加秒杀商品---->redis"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        Map<String, Object> map = new HashMap<String, Object>();
        //创建操作redis中list的模板
        ListOperations lop = redisTemplate.opsForList();
        //获取秒杀的商品集合
        List<WebSeckillEntity> seckills = webSeckillMapper.selectSecKillByTimes();
        if(seckills.size()!=0){
            for (WebSeckillEntity seckill:seckills) {  //secId_proId
                String key = seckill.getId()+"_"+seckill.getProductid();
                //不存在则装入
                if(!redisTemplate.hasKey(key)){
                    map.put("addSecKillToRedisCode",200);
                    System.out.println(key+"此次已装入redis..");
                    for (int i=0;i<seckill.getNum();i++){
                        //装入到redis中
                        lop.leftPush(key,seckill.getId()+","+seckill.getProductid());
                    }
                }else {
                    map.put("addSecKillToRedisCode",202);
                    System.out.println(key+"以前已装入redis..");
                }
            }
        }else {
            map.put("addSecKillToRedisCode",404);
            System.out.println("当前时间没有商品被装入");
        }
        return map;
    }

    //2.将即将开始的秒杀商品的状态由0(未开始)---->1(已开始)
    @Scheduled(cron = "0/2 * * * * ? ") // 间隔2秒执行
    @Override
    public Integer updateUPSecKillStatus() throws Exception {
        System.out.println("系统当前时间执行修改状态0---->1"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        Integer count = webSeckillMapper.updateUPSecKillStatus();
        System.out.println("此修改了"+count+"个");
        return count;
    }

    //3.开始进行秒杀
    @Override
    public Map<String, Object> doSecKill(Long secId,String token) throws Exception {
        //声明用户id
        Long uid = null;
        //根据令牌找到登陆用户数据
        if(redisTemplate.hasKey("sessionid:" + token)){
            //得到操作字符串的模板
            ValueOperations vop = redisTemplate.opsForValue();
            WebUsersEntity loginUser = (WebUsersEntity)vop.get("sessionid:" + token);
            uid = loginUser.getId();
        }
       /* System.out.println("uid="+uid+"进行了秒杀。。");
       Long loinUserId = null;
       SetOperations sop = redisTemplate.opsForSet();
        if(sop.isMember("loginUser", uid)){
            loinUserId = uid;
        }
        Map<String, Object> map = new HashMap<String, Object>();*/
        map.put("uid",uid);
        map.put("code",400);
        map.put("msg","秒杀失败！！");
        //1.判断是否登陆
        if(uid!=null){  //已登陆
            //2.根据秒杀id查询秒杀商品
            WebSeckillEntity webSeckillEntity = baseMapper.queryObjectById(Integer.valueOf(secId.toString()));
            //3.判断秒杀商品状态
            if("0".equals(webSeckillEntity.getStatus())){  //秒杀未开始
                map.put("code",404);
                map.put("msg","秒杀未开始！！");
            }
            if("2".equals(webSeckillEntity.getStatus())){  //秒杀已结束
                map.put("code",405);
                map.put("msg","秒杀已结束！！");
            }
            if("1".equals(webSeckillEntity.getStatus())){  //开始进行秒杀
                //得到操作list的模板
                ListOperations lop = redisTemplate.opsForList();
                //4.判断redis中的list还有没有被秒杀的商品
                String listkey = webSeckillEntity.getId()+"_"+webSeckillEntity.getProductid();
                //5.删除list中的一个元素，返回被删除的元素
                Object obj = lop.leftPop(listkey);
                //6.若返回值不为空，则删除成功，反映出之前有商品
                if(obj!=null){  //存在商品并已经删除一个
                    //7.判断此用户是否之前有抢到秒杀商品
                    //8.得到操作redis中set集合的模板
                    SetOperations sop = redisTemplate.opsForSet();
                  //  sop = redisTemplate.opsForSet();
                    //9.得到set集合数据中的key   secKill_1_10001
                    String setkey = "secKill_"+webSeckillEntity.getId()+"_"+webSeckillEntity.getProductid();
                    //10.得到set中的value  1,101,10001
                    String setvalue = webSeckillEntity.getId()+","+uid+","+webSeckillEntity.getProductid();
                    if(!sop.isMember(setkey,setvalue)){  //11.不存在,则加入，表示秒杀成功
                        sop.add(setkey,setvalue);  //装入到redis
                        //装入到rabbitMQ
                        rabbitMQService.addRabbitMQToExC(webSeckillEntity.getId(),webSeckillEntity.getProductid(),webSeckillEntity.getSeckillprice(),uid);
                        //往ribbonMQ中存放数据  secId   uid  proId
                        map.put("code",200);
                        map.put("msg","恭喜你，秒杀成功！！");
                    }else {  //存在
                        lop.leftPush(listkey,obj);   //把商品重新加回商品的list中
                        map.put("code",407);
                        map.put("msg","对不起，你已经秒杀过了，不能重复秒杀！！");
                    }
                }else {
                    map.put("code",406);
                    map.put("msg","商品已全部秒杀完，欢迎下次继续秒杀！！");
                }
            }
        }else {
            map.put("code",202);
            map.put("msg","用户未登录，不能秒杀！！");
        }

        return map;
    }

    //4.将即将开始的秒杀商品的状态由1(已开始)---->2(已结束)
 //   @Scheduled(cron = "0/2 * * * * ? ") // 间隔2秒执行
    @Override
    public Integer updateEndSecKillStatus() throws Exception {
        System.out.println("系统当前时间执行修改状态1---->2"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        Integer count = webSeckillMapper.updateEndSecKillStatus();
        System.out.println("此修改了"+count+"个");
        return count;
    }

    //加载要秒杀的商品信息
    @Override
    public List<Map<String, Object>> findUPSecKill() throws Exception {
        List<Map<String,Object>> dataList = null;
        //查询得到可以秒杀的数据
        WebSeckillEntity selKillPra = new WebSeckillEntity();
        selKillPra.setStatus("1");
        List<WebSeckillEntity> secKills = baseMapper.queryManyByPramas(selKillPra);
        //进行循环加上商品数据
        if(secKills!=null){
            dataList = new ArrayList<Map<String, Object>>();
            for (WebSeckillEntity seckill:secKills) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("secId",seckill.getId());
                map.put("proId",seckill.getProductid());
                map.put("secPrice",seckill.getSeckillprice());
                map.put("endTime",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(seckill.getEndtime()));
                //商品数据查询
                WebProductDetailEntity productDetail = webProductDetailMapper.queryObjectById(Integer.parseInt(seckill.getProductid().toString()));
                map.put("title",productDetail.getTitle());
                map.put("subTitle",productDetail.getSubtitle());
                map.put("price",productDetail.getPrice());
                map.put("avthorImg",productDetail.getAvatorimg());
                dataList.add(map);
            }
        }
        return dataList;
    }

    /*//模拟登陆
    @Override
    public Map<String, Object> login(Long uid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("code",400);
        SetOperations sop = redisTemplate.opsForSet();
        Long count = sop.add("loginUser", uid);
        if(count>0){
            map.put("code",200);
        }
        return map;
    }*/


}
