package cn.com.java.web.products.service.impl;

import cn.com.java.model.WebProductDetailEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 *    WebProductDetail业务层实现类
 * @date 2019-12-09 10:27:01
 */
@Service
@Transactional
public class WebProductDetailServiceImpl extends BaseServiceImpl<WebProductDetailEntity> implements WebProductDetailService {

    //需要依赖注入FreeMarker模板对象
    @Autowired
    private Configuration configuration;

    //创建商品详情的静态页面
    @Scheduled(cron = "0/10 * * * * ? ") // 间隔10秒执行
    @Override
    public String makeHtmls(){
        try {
            //1.得到生成静态页面的模板对象
            Template template = configuration.getTemplate("Product.ftl");
            //2.查询所有的商品信息
            List<WebProductDetailEntity> products = baseMapper.queryAll();
            for (WebProductDetailEntity product:products) {
                //3.定义生成静态页面的文件夹路径
                String filePath = "D:\\k9508\\products\\"+product.getId()+".html";
                //4.通过绝对路径新建一个目标文件
                File newFile = new File(filePath);
                //5.得到目标文件的字符输出流对象
                FileWriter fw = new FileWriter(newFile);
                //6.得到动态数据
                Map<String,Object> dataMap = new HashMap<String, Object>();
                dataMap.put("title",product.getTitle());
                dataMap.put("goodId",product.getId());
                dataMap.put("href",product.getHref());
                dataMap.put("avatorimg",product.getAvatorimg());
                //查询出商品的轮播图
                dataMap.put("imgUrlList",webProductImgMapper.selWebImgUrlByProId(product.getId()));
                dataMap.put("subTitle",product.getSubtitle());
                dataMap.put("price",product.getPrice());
                dataMap.put("type",product.getType());
                dataMap.put("color",product.getColor());
                dataMap.put("num",product.getNum());
                //7.通过模板对象完成静态html页面的生成
                template.process(dataMap,fw);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
