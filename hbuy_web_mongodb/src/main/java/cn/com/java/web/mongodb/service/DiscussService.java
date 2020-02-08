package cn.com.java.web.mongodb.service;

import org.bson.Document;
import java.util.List;

/**
 *   评论的业务层接口
 */
public interface DiscussService {

    //查询所有的商品评论
    List<Document> findAllDiscuss() throws Exception;
}
