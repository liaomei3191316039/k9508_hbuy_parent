package cn.com.java.web.provider.impl;

import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    //测试ribbon的方法
    @Override
    public String testRibbon(String userName) throws Exception {
        System.out.println("此时走的是provider2的模块! ! !");
        return userName;
    }
}
