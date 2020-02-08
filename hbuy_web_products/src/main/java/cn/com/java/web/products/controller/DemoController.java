package cn.com.java.web.products.controller;

import cn.com.java.model.WebProductDetailEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *   控制器案例
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    //需要依赖注入FreeMarker模板对象
    @Autowired
    private Configuration configuration;

    //跳转到test01.ftl模板中去
    @RequestMapping("/test01")
    public ModelAndView test01(ModelAndView modelAndView){
        modelAndView.setViewName("test01");
        //1.简单数据的传递
        modelAndView.addObject("userName","张三");
        modelAndView.addObject("num",120);
        modelAndView.addObject("price",123.90d);
        modelAndView.addObject("nowDate",new Date());
        //2.对象的传值
        WebProductDetailEntity productDetail = new WebProductDetailEntity();
        productDetail.setId(10001l);
        productDetail.setSubtitle("OPPO手机");
        productDetail.setPrice(1900f);
        productDetail.setUpdatetime(new Date());
        productDetail.setColor("白色");
        modelAndView.addObject("productDetail",productDetail);
        //3.集合的传值（list）
        List<String> strs = Arrays.asList("qwe","rty","asd","zxc","890");
        modelAndView.addObject("strs",strs);
        //4.map集合的传值
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("pwd","123123");
        dataMap.put("price",123.90);
        dataMap.put("createDate",new Date());
        dataMap.put("num",120);
        modelAndView.addObject("dataMap",dataMap);
        return modelAndView;
    }

    //通过Product.ftl模板生成静态html页面
    @RequestMapping("/makeHtml/{fileName}")
    public @ResponseBody String makeHtml(@PathVariable("fileName") String fileName){
        //1.定义生成静态页面的文件夹路径
        String filePath = "D:\\k9508\\products\\"+fileName+".html";
        //2.通过绝对路径新建一个目标文件
        File newFile = new File(filePath);
        try {
            //3.得到目标文件的字符输出流对象
            FileWriter fw = new FileWriter(newFile);
            //4.得到生成静态页面的模板对象
            Template template = configuration.getTemplate("Product.ftl");
            //5.得到动态数据
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("title","OPPO手机");
            dataMap.put("goodId",10002l);
            dataMap.put("href","https://www.baidu.com/");
            dataMap.put("avatorimg","http://q1cydzrcd.bkt.clouddn.com/c36b3f60efe84ac493aba62bf0c4ad8e");
            dataMap.put("imgUrlList",Arrays.asList("http://q1cydzrcd.bkt.clouddn.com/c36b3f60efe84ac493aba62bf0c4ad8e"
            ,"http://q1cydzrcd.bkt.clouddn.com/6038c7678afe4a349b08d101ebb728f9","http://q1cydzrcd.bkt.clouddn.com/9fefcbbfff9c45f88b9b487c31a7dd65"));
            dataMap.put("subTitle","OPPO最新版智能机");
            dataMap.put("price",1890.9d);
            dataMap.put("type","智能拍照手机");
            dataMap.put("color","粉色");
            dataMap.put("num",390);
            //6.通过模板对象完成静态html页面的生成
            template.process(dataMap,fw);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } catch (TemplateException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
