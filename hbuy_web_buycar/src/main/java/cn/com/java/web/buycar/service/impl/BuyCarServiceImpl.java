package cn.com.java.web.buycar.service.impl;

import cn.com.java.model.BuyCar;
import cn.com.java.model.Good;
import cn.com.java.model.WebProductDetailEntity;
import cn.com.java.model.WebUsersEntity;
import cn.com.java.web.buycar.mapper.WebProductDetailMapper;
import cn.com.java.web.buycar.utils.Base64Utils;
import cn.com.java.web.service.BuyCarService;
import cn.com.java.web.service.RabbitMQService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *   购物车业务层实现类
 */
@Service
@Transactional(readOnly = false)
public class BuyCarServiceImpl implements BuyCarService {

    @Autowired
    private WebProductDetailMapper webProductDetailMapper;

    //redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;

    //添加购物车
    @Override
    public Map<String, Object> addBuyCar(Integer goodId, Integer num, String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //cookie的区分
        //1.根据服务器：http://localhost:8089
        //2.根据设置产生cookie的路径：buyCarCookie.setPath("/buyCar")与控制器的访问路径一致;
        //3.根据cookie的name进行区分
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodId",goodId);
        map.put("num",num);
        map.put("code","fail");
        //通过redis缓存模板得到缓存对象
        ValueOperations vop = redisTemplate.opsForValue();
        Long uid = null;
        if(!"".equals(token)){
            if(redisTemplate.hasKey("sessionid:" + token)){
                WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionid:" + token);
                uid = loginUser.getId();
            }
        }
        map.put("uid",uid);
        //判断user对象是否存在
        if(uid==null){  //没有登录
            //得到整个页面中的cookies
            Cookie[] cookies = request.getCookies();
            //定义一个空的buyCar购物车的cookie
            Cookie buyCarCookie = null;
            //判断cookies是否存在cookie
            if(cookies!=null){
                //找到名字为buyCar的cookie是否存在
                for(int i=0;i<cookies.length;i++){
                    //取到cookie的名字
                    String cookieName = cookies[i].getName();
                    //判断名字中是否存在"buyCar"
                    if("buyCar".equals(cookieName)){
                        //存在，则得到购物车cookie
                        buyCarCookie = cookies[i];
                        break;
                    }
                }
            }
            //判断buyCar的Cookie是否存在
            if(buyCarCookie==null){   //购物车的cookie不存在
                System.out.println("页面中没有登录的情况下第1次添加购物车。。");
                //得到要添加的商品对象
                Good good= new Good(goodId,num);
                //新建购物车对象
                BuyCar buyCar = new BuyCar();
                //将要添加的商品加入到购物车对象中
                List<Good> goods = new ArrayList<Good>();
                goods.add(good);
                buyCar.setGoods(goods);
                //重点：对购物车进加密
                //加密之前要将购物车对象转为字符串（将购物车对象JSON化）
                String buyCarStr = JSON.toJSONString(buyCar);
                System.out.println(buyCarStr);
                //将转后的JSON数据进行加密操作
                String baseBuyCar = Base64Utils.getBASE64(buyCarStr);
                //新建购物车cookie
                buyCarCookie = new Cookie("buyCar",baseBuyCar);
                //设置cookie的有效时间
                buyCarCookie.setMaxAge(3600*32);
                //响应回客户端
                response.addCookie(buyCarCookie);
                map.put("code","success");
            }else{  //购物车的buyCarCookie存在
                System.out.println("存在buyCar的cookie..");
                //取出购物车buyCarCookie中的value
                String baseBuyCar = buyCarCookie.getValue();
                BuyCar buyCar = null;  //定义购物车对象
                List<Good> goods = null;  //定义商品集合对象
                //判断buyCarCookie中value是否为空
                if(!StrUtil.isBlank(baseBuyCar)&&!StrUtil.isNullOrUndefined(baseBuyCar)){
                    System.out.println("存在buyCar的cookie..且cookie中有value值。。");
                    //表示存在值，不为空，解密得到购物车商品List集合
                    buyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseBuyCar),BuyCar.class);
                    goods = buyCar.getGoods();
                    //判断后面加入的商品id有没有与之前购物车中的商品id重复
                    Good good = null;
                    int i = 0;
                    for (;i<goods.size();i++){
                        if(goods.get(i).getGoodId().equals(goodId)){
                            good = goods.get(i);
                            break;
                        }
                    }
                    if(good!=null){
                        //判断新加入的商品ID与购物车中的商品ID有重复,只是修改原有商品的数量
                        int newNum = good.getNum()+num;
                        goods.remove(good);
                        goods.add(i,new Good(goodId,newNum));
                        System.out.println("存在buyCar的cookie..且cookie中有value值。。且新加入的与之前有重复。。");
                    }else {
                        goods.add(new Good(goodId,num));
                        System.out.println("存在buyCar的cookie..且cookie中有value值。。且新加入的与之前没有有重复。。");
                    }
                }else {
                    //value值为空
                    //新建商品集合
                    goods = Arrays.asList(new Good(goodId,num));
                    //新建购物车对象
                    buyCar = new BuyCar();
                }
                //将商品集合重新设置到购物车对象中
                buyCar.setGoods(goods);
                //购物车对象进行加密,再将购物车cookie响应给客户端
                String buyCarStr = JSON.toJSONString(buyCar);
                System.out.println(buyCarStr);
                String miwen = Base64Utils.getBASE64(buyCarStr);
                //将加密的换行操作去掉
                miwen = miwen.replaceAll("\r\n","");
                buyCarCookie = new Cookie("buyCar",miwen);
                //设置cookie的有效时间
                buyCarCookie.setMaxAge(3600*32);
                //设置cookie的路径要与控制器的访问路径一致
                buyCarCookie.setPath("/buyCar");
                //响应回客户端
                response.addCookie(buyCarCookie);
                map.put("code","success");
            }
        }else {  //已登录
            //得到整个页面中的cookies
            Cookie[] cookies = request.getCookies();
            //定义一个空的buyCar购物车的cookie
            Cookie buyCarCookie = null;
            //判断cookies是否存在cookie
            if (cookies != null) {
                //找到名字为buyCar的cookie是否存在
                for (int i = 0; i < cookies.length; i++) {
                    //取到cookie的名字
                    String cookieName = cookies[i].getName();
                    //判断名字中是否存在"buyCar"
                    if ("buyCar".equals(cookieName)) {
                        //存在，则得到购物车cookie
                        buyCarCookie = cookies[i];
                        break;
                    }
                }
            }
            BuyCar buyCar = null;  //定义购物车对象
            List<Good> goods = null;  //定义商品集合对象
            //判断buyCar的Cookie是否存在
            if (buyCarCookie == null) {   //购物车的cookie不存在
                //直接加入redis中
                Good good1 = null;
                int j = 0;
                String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
                //判断redis中的是否存在buyCar数据
                if(!StrUtil.isBlank(redisBuyCarStr)&&!StrUtil.isNullOrUndefined(redisBuyCarStr)){
                    buyCar = JSON.parseObject(redisBuyCarStr,BuyCar.class);
                    List<Good> redisGoods = buyCar.getGoods();
                    for (;j<redisGoods.size();j++){
                        if(goodId.equals(redisGoods.get(j).getGoodId())){
                            good1 = redisGoods.get(j);
                            break;
                        }
                    }
                    if(good1==null){
                        redisGoods.add(new Good(goodId,num));
                    }else {
                        int newNum = good1.getNum()+num;
                        redisGoods.remove(good1);
                        redisGoods.add(j,new Good(goodId,newNum));
                    }
                    buyCar.setGoods(redisGoods);
                }else {
                    buyCar = new BuyCar();
                    buyCar.setGoods(Arrays.asList(new Good(goodId,num)));
                }
                vop.set("redisBuyCar," + uid,JSON.toJSONString(buyCar));
                map.put("code","success");
            } else {
                //购物车的buyCarCookie存在
                //取出购物车buyCarCookie中的value
                String baseBuyCar = buyCarCookie.getValue();
                //判断buyCarCookie中value是否为空
                if (!StrUtil.isBlank(baseBuyCar) && !StrUtil.isNullOrUndefined(baseBuyCar)) {
                    //表示存在值，不为空，解密得到购物车商品List集合
                    BuyCar buyCarCookie1 = JSON.parseObject(Base64Utils.getFromBASE64(baseBuyCar), BuyCar.class);
                    List<Good> goodsCookie = buyCarCookie1.getGoods();
                   /* //判断后面加入的商品id有没有与之前购物车中的商品id重复
                    Good good = null;
                    int i = 0;
                    for (; i < goodsCookie.size(); i++) {
                        if (goodsCookie.get(i).getGoodId().equals(goodId)) {
                            good = goodsCookie.get(i);
                            break;
                        }
                    }
                    if (good != null) {  //cookie中有购物车商品
                        int newNum = good.getNum()+num;
                        goodsCookie.remove(good);
                        goodsCookie.add(i,new Good(goodId,newNum));
                    } else {
                        goodsCookie.add(new Good(goodId,num));
                    }*/
                    //判断redis中是否存在buyCar数据
                    String redisBuycarStr1 = (String) vop.get("redisBuyCar," + uid);
                    if(!StrUtil.isBlank(redisBuycarStr1) && !StrUtil.isNullOrUndefined(redisBuycarStr1)){
                        BuyCar redisBuycar1 = JSON.parseObject(redisBuycarStr1,BuyCar.class);
                        //得到redis中的集合
                        List<Good> goodsRedis1 = redisBuycar1.getGoods();
                        Good newGood = null;
                        //新建一个空的商品集合
                        List<Good> newList = new ArrayList<Good>();
                        for (int k=0;k<goodsRedis1.size();k++){  //循环redis中的商品   1001,1002,1003,1004
                            for (int l=0;l<goodsCookie.size();l++){    //循环cookie中的商品   1002,1004,1006,1008
                                if(goodsRedis1.get(k).getGoodId().equals(goodsCookie.get(l).getGoodId())){
                                    newGood = new Good(goodsRedis1.get(k).getGoodId(),goodsRedis1.get(k).getNum()+goodsCookie.get(l).getNum());
                                    goodsCookie.remove(goodsCookie.get(l));
                                    break;
                                }else {
                                    newGood = goodsRedis1.get(k);
                                }
                            }
                            newList.add(newGood);
                        }
                        if(goodsCookie.size()>0){
                            //加入cookie中新加入的good
                            newList.addAll(goodsCookie);
                        }
                        //重新加入到redis中
                        redisBuycar1.setGoods(newList);
                        System.out.println(JSON.toJSONString(redisBuycar1));
                        vop.set("redisBuyCar," + uid,JSON.toJSONString(redisBuycar1));
                        map.put("code","success");
                    }else {  //redis没有buyCar的数据
                        buyCarCookie1.setGoods(goodsCookie);
                        vop.set("redisBuyCar," + uid,JSON.toJSONString(buyCarCookie1));
                        map.put("code","success");
                    }
                    //清空buyCar的cookie
                    //清空cookie
                    Cookie cookie = new Cookie("buyCar","");
                    cookie.setPath("/buyCar");//此处与109行代码保持一致，否则cookie清空会失败
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }else {//cookie中的value值为空
                    //直接加入redis中
                    Good good1 = null;
                    int j = 0;
                    String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
                    //判断redis中的是否存在buyCar数据
                    if(!StrUtil.isBlank(redisBuyCarStr)&&!StrUtil.isNullOrUndefined(redisBuyCarStr)){
                        BuyCar redisBuyCar = JSON.parseObject(redisBuyCarStr,BuyCar.class);
                        List<Good> redisGoods = redisBuyCar.getGoods();
                        for (;j<redisGoods.size();j++){
                            if(goodId.equals(redisGoods.get(j).getGoodId())){
                                good1 = redisGoods.get(j);
                                break;
                            }
                        }
                        if(good1==null){
                            redisGoods.add(new Good(goodId,num));
                        }else {
                            int newNum = good1.getNum()+num;
                            redisGoods.remove(good1);
                            redisGoods.add(j,new Good(goodId,newNum));
                        }
                        buyCar.setGoods(redisGoods);
                    }else {
                        buyCar = new BuyCar();
                        buyCar.setGoods(Arrays.asList(new Good(goodId,num)));
                    }
                    vop.set("redisBuyCar," + uid,JSON.toJSONString(buyCar));
                    map.put("code","success");
                }
            }
        }
        return map;
    }

    //登陆更新购物车
    @Override
    public Map<String, Object> loginAndUpdBuyCar(String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //通过redis缓存模板得到缓存对象
        ValueOperations vop = redisTemplate.opsForValue();
        Long uid = null;
        if(!"".equals(token)){
            if(redisTemplate.hasKey("sessionid:" + token)){
                WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionid:" + token);
                uid = loginUser.getId();
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code","loginSuccess:"+uid);
        //登录时候将cookie存在的购物车数据加入到redis
        //得到整个页面中的cookies
        Cookie[] cookies = request.getCookies();
        // System.out.println(cookies.length);
        //定义一个空的buyCar购物车的cookie
        Cookie buyCarCookie = null;
        //判断cookies是否存在cookie
        if(cookies!=null){
            //找到名字为buyCar的cookie是否存在
            for(int i=0;i<cookies.length;i++){
                //取到cookie的名字
                String cookieName = cookies[i].getName();
                System.out.println(cookieName);
                //判断名字中是否存在"buyCar"
                if("buyCar".equals(cookieName)){
                    //存在，则得到购物车cookie
                    buyCarCookie = cookies[i];
                    break;
                }
            }
        }
        if(buyCarCookie!=null){
            System.out.println("buyCarCookie不为空。。");
            //取出购物车buyCarCookie中的value
            String baseBuyCar = buyCarCookie.getValue();
            //判断cookie中的value是否为空
            if(!StrUtil.isBlank(baseBuyCar)&&!StrUtil.isNullOrUndefined(baseBuyCar)){
                //得到cookie中的购物车数据
                BuyCar cookieBuyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseBuyCar),BuyCar.class);
                List<Good> goodsCookie = cookieBuyCar.getGoods();
                //得到redis的key对应的value
                String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
                //判断redis中value是否为空
                if(!StrUtil.isBlank(redisBuyCarStr)&&!StrUtil.isNullOrUndefined(redisBuyCarStr)){
                    if(!redisBuyCarStr.equals("")){//判断value是否为空字符串
                        //得到cookie
                        BuyCar redisBuycar1 = JSON.parseObject(redisBuyCarStr,BuyCar.class);
                        //得到redis中的集合
                        List<Good> goodsRedis1 = redisBuycar1.getGoods();
                        Good newGood = null;
                        //新建一个空的商品集合
                        List<Good> newList = new ArrayList<Good>();
                        for (int k=0;k<goodsRedis1.size();k++){  //循环redis中的商品   1001,1002,1003,1004
                            for (int l=0;l<goodsCookie.size();l++){    //循环cookie中的商品   1002,1004,1006,1008
                                if(goodsRedis1.get(k).getGoodId().equals(goodsCookie.get(l).getGoodId())){
                                    newGood = new Good(goodsRedis1.get(k).getGoodId(),goodsRedis1.get(k).getNum()+goodsCookie.get(l).getNum());
                                    goodsCookie.remove(goodsCookie.get(l));
                                    break;
                                }else {
                                    newGood = goodsRedis1.get(k);
                                }
                            }
                            newList.add(newGood);  //newList:1001，1002，1003，1004   goodsCookie:1004，1006，1008
                        }
                        if(goodsCookie.size()>0){
                            //加入cookie中新加入的good
                            newList.addAll(goodsCookie);  //newList:1001，1002，1003，1004，1006，1008
                        }
                        //重新加入到redis中
                        redisBuycar1.setGoods(newList);
                        System.out.println(JSON.toJSONString(redisBuycar1));
                        vop.set("redisBuyCar," + uid,JSON.toJSONString(redisBuycar1));
                    }
                }else {  //cookie中有数据，redis中没有数据
                    vop.set("redisBuyCar," + uid,JSON.toJSONString(cookieBuyCar));
                }
            }
            //清空cookie
            Cookie cookie = new Cookie("buyCar","");
            cookie.setPath("/buyCar");//此处与133行代码保持一致，否则cookie清空会失败
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return map;
    }

    //加载购物车
    @Override
    public List<Map<String, Object>> loadBuyCar(String token, HttpServletRequest request) throws Exception {
        ValueOperations vop = redisTemplate.opsForValue();
        Long uid = null;
        List<Good> goods = null;
        List<Map<String,Object>> dataList = null;
        //1-1.判断是否登陆
        //1-2.从redis中取登陆用户数据
        if(redisTemplate.hasKey("sessionid:" + token)){
            WebUsersEntity loginUser = (WebUsersEntity)vop.get("sessionid:" + token);
            uid = loginUser.getId();
        }
        //1-3.判断uid是否为null
        if(uid!=null){
            //1-4.此时已登陆
            //1-5.取出redis中的购物车数据的字符串
            if(redisTemplate.hasKey("redisBuyCar," + uid)){
                String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
                if(!StrUtil.isBlank(redisBuyCarStr)&&!StrUtil.isNullOrUndefined(redisBuyCarStr)){
                    //1-6.将其转为BuyCar实体对象
                    BuyCar redisBuyCar = JSON.parseObject(redisBuyCarStr,BuyCar.class);
                    //1-7.取到list集合
                    goods = redisBuyCar.getGoods();
                    System.out.println("此时有登陆！！");
                    for (Good good:goods) {
                        System.out.println(good);
                    }
                }
            }
        }else{
            //2-1.没有登陆，找购物车cookie
            //2-2.取到所有cookie
            Cookie buyCarCookie = null;
            Cookie[] cookies = request.getCookies();
            //2-3.获取购物车cookie
            if(cookies!=null){
                for (Cookie cookie:cookies) {
                    if(cookie.getName().equals("buyCar")){
                        buyCarCookie = cookie;
                        break;
                    }
                }
            }
            //存在购物车cookie
            if(buyCarCookie!=null){
                //取到购物车cookie中的数据
                String cookieBuyCarMStr = buyCarCookie.getValue();
                //数据是否有效
                if(!StrUtil.isBlank(cookieBuyCarMStr)&&!StrUtil.isNullOrUndefined(cookieBuyCarMStr)){
                    //有效数据，加密---->解密
                    String cookieBuyCarStr = Base64Utils.getFromBASE64(cookieBuyCarMStr);
                    //转成对象BuyCar
                    BuyCar cookieBuyCar = JSON.parseObject(cookieBuyCarStr,BuyCar.class);
                    //1-7.取到list集合
                    goods = cookieBuyCar.getGoods();
                    System.out.println("此时没有登陆。。");
                    for (Good good:goods) {
                        System.out.println(good);
                    }
                }
            }

        }

        //3-1.存在购物车商品数据
        if(goods!=null){
            dataList = new ArrayList<Map<String, Object>>();
            //通过List<Good>   ------->    List<Map<String,Object>>   访问mysql
            for (Good good:goods) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("num",good.getNum());  //装入数量
                //查询Mysql数据库,根据id查询商品详细数据
                WebProductDetailEntity productDetail = webProductDetailMapper.queryObjectById(good.getGoodId());
                map.put("avatorimg",productDetail.getAvatorimg());  //商品封面图
                map.put("proId",productDetail.getId());
                map.put("price",productDetail.getPrice());  //单价
                map.put("title",productDetail.getTitle());  //标题
                map.put("subtitle",productDetail.getSubtitle());  //详细标题
                map.put("zprice",productDetail.getPrice()*good.getNum());  //总价
                dataList.add(map);
            }
        }
        return dataList;
    }

    //添加订单-->RabbitMQ中
    @Override
    public Map<String, Object> addToMQ(String token, String proIds, Float allPrice) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",404);
        map.put("msg","提交订单失败！！");
        //声明用户id
        Long uid = null;
        //根据令牌找到登陆用户数据
        if(redisTemplate.hasKey("sessionid:" + token)){
            //得到操作字符串的模板
            ValueOperations vop = redisTemplate.opsForValue();
            WebUsersEntity loginUser = (WebUsersEntity)vop.get("sessionid:" + token);
            uid = loginUser.getId();
        }
        if(uid!=null){
            rabbitMQService.addRabbitMQToBuyCar(proIds,allPrice,uid);
            map.put("code",200);
            map.put("msg","提交订单成功。。");
            //把原有的redis中的购物车数据删除掉.....
        }else {
            map.put("code",500);
            map.put("msg","用户未登陆，无法提交订单！！");
        }
        return map;
    }

    public static void main(String[] args) {
        try {
            new BuyCarServiceImpl().loadBuyCar("",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
