package cn.com.java.web.mongodb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *   mongoDB数据库工具类
 */
public class MongoUtil {

    //mongodb的数据库的客户端对象
    private static MongoClient client = null;
    //mongodb的数据库的某库中某一个集合对象
    private static MongoCollection<Document> userCollection=null;
    //连接数据库的ip
    private static String host = null;
    //连接数据库的端口
    private static Integer port = null;
    //连接数据库的的名字
    private static String databaseName = null;
    //连接数据库的某库中的集合名字
    private static String collectionName = null;

    static{
        try {
            //1、创建Properties对象来表示mongo.properties文件
            Properties prop = new Properties();
            //2、关联
            InputStream ins = MongoUtil.class.getClassLoader().getResourceAsStream("mongo.properties");
            prop.load(ins);
            //3、获取文件中的数据
            host = prop.getProperty("host");
            port = Integer.parseInt(prop.getProperty("port")) ;
            databaseName = prop.getProperty("databaseName");
            collectionName = prop.getProperty("collectionName");
            //4、关流
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取集合
     * @return
     */
    public static MongoCollection<Document> getCollection(){
        //1、连接上MongoDB
        client = new MongoClient(host,port);
        //2、连接上指定的库
        MongoDatabase db = client.getDatabase(databaseName);
        //3、连上指定的集合
        userCollection = db.getCollection(collectionName);
        return userCollection;
    }

    /**
     * 关闭MongoDB的客户端
     */
    public static void close(){
        if(client!=null)
            client.close();
    }

    public static void main(String[] args) {
        System.out.println(getCollection());
    }
}
