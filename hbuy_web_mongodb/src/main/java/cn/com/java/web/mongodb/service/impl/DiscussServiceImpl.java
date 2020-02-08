package cn.com.java.web.mongodb.service.impl;

import cn.com.java.web.mongodb.service.DiscussService;
import cn.com.java.web.mongodb.utils.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscussServiceImpl implements DiscussService {

    //查询所有的商品评论
    @Override
    public List<Document> findAllDiscuss() throws Exception {
        //获取操作mongoDB的集合的对象
        MongoCollection<Document> collection = MongoUtil.getCollection();
        //查询得到文档数据的迭代器对象
        FindIterable<Document> documents = collection.find();
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
        //新建文档数据的List集合
        List<Document> list = new ArrayList<Document>();
        //将文档数据通过循环加入到list集合中
        documents.iterator().forEachRemaining(temp -> list.add(temp));
        return list;
    }
}
