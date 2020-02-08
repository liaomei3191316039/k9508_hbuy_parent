package cn.com.java.web.mongodb.test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 *   MongoDB数据库的测试类
 */
public class MongoDBTest {

    //得到操作MongoDB数据库集合的对象
    //声明MongoDB数据库客户端
    private MongoClient mongoClient = null;
    //声明MongoDB数据库库
    private MongoDatabase mongoDatabase = null;
    //声明MongDB数据库操作集合
    private MongoCollection<Document> mongoCollection = null;

    //测试之前得到操作MongDB数据库操作集合的对象
    @Before
    public void init(){
        //获取MongoDB数据库客户端对象
        mongoClient = new MongoClient("127.0.0.1:27017");
        //获取MongoDB数据库库对象
        mongoDatabase = mongoClient.getDatabase("mydb");
        //得到MongDB数据库操作集合对象
        mongoCollection = mongoDatabase.getCollection("books");
    }

    //测试是否成功得到操作MongDB数据库集合对象
    @Test
    public void test01(){
        System.out.println(mongoCollection);
    }

    //查询集合中所有的文档数据
    @Test
    public void test02(){
        //返回文档数据的迭代器对象
        FindIterable<Document> documents = mongoCollection.find();
        //遍历文档数据的迭代器（此时遍历的是文档（Bson数据格式））
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("--------------------------------------------");
        //新建list文档数据集合
        List<Document> list = new ArrayList<Document>();
        //将每一个文档数据对象装入到集合中
        documents.iterator().forEachRemaining(temp -> list.add(temp));
        //遍历文档数据的list集合
        for (Document document:list) {
            System.out.println(document);
        }
    }

    //多条件查询(查询范围price小于90或者price大于110并且作者名字中存在a(不区分大小写)的书籍)条件外层有and
    @Test
    public void test03(){
        //新建查询的条件（直接将MongoDB查询的语句拿来）
        Document documentPra = Document.parse("{\n" +
                "   $and:[\n" +
                "      { \n" +
                "        $or:[\n" +
                "         {'price':{$lte:90}},\n" +
                "         {'price':{$gte:110}}\n" +
                "        ]\n" +
                "      },\n" +
                "      { \n" +
                "        'author':{$regex:/a/i}\n" +
                "      }\n" +
                "   ]\n" +
                "}");
        //执行查询得到文档数据迭代器对象
        FindIterable<Document> documents = mongoCollection.find(documentPra);
        //遍历文档数据迭代器对象
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //根据bname和author查询
    @Test
    public void test04(){
        //新建查询的条件
        Document documentPra = new Document();
        //拼接查询条件书名
        documentPra.append("bname","三国演义");
        //拼接查询条件作者
        documentPra.append("author","罗贯中");
        //执行查询得到文档数据迭代器对象
        FindIterable<Document> documents = mongoCollection.find(documentPra);
        //遍历文档数据迭代器对象
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //分页查询
    @Test
    public void test05(){
        System.out.println("------------------第1页--------------------");
        //第1页
        FindIterable<Document> document1 = mongoCollection.find().skip((1 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document1.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("------------------第2页--------------------");
        //第2页
        FindIterable<Document> document2 = mongoCollection.find().skip((2 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document2.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("------------------第3页--------------------");
        //第3页
        FindIterable<Document> document3 = mongoCollection.find().skip((3 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document3.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("------------------第4页--------------------");
        //第4页
        FindIterable<Document> document4 = mongoCollection.find().skip((4 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document4.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //根据bname和author查询(调用的是方法)
    @Test
    public void test06(){
        //新建查询书名的条件
        Bson bsonBname = Filters.in("bname","三国演义");
        //新建查询作者的条件
        Bson bsonAuthor = Filters.in("author","罗贯中");
        //将两个条件合并
        Bson bsonPra = Filters.and(bsonBname,bsonAuthor);
        //执行条件查询
        FindIterable<Document> documents = mongoCollection.find(bsonPra);
        //遍历文档数据迭代器对象
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //根据bname和person查询,进行分页(调用的是方法)
    @Test
    public void test07(){
        //新建查询书名的条件
        Bson bsonBname = Filters.in("bname","水浒传09");
        //新建查询人物的条件
        Bson bsonPerson = Filters.in("person","宋江");
        //将两个条件合并
        Bson bsonPra = Filters.and(bsonBname,bsonPerson);
        System.out.println("-----------------第1页------------------------");
        //进行分页查询
        FindIterable<Document> document1 = mongoCollection.find(bsonPra).skip((1 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document1.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("-----------------第2页------------------------");
        //进行分页查询
        FindIterable<Document> document2 = mongoCollection.find(bsonPra).skip((2 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document2.iterator().forEachRemaining(temp -> System.out.println(temp));
        System.out.println("-----------------第3页------------------------");
        //进行分页查询
        FindIterable<Document> document3 = mongoCollection.find(bsonPra).skip((3 - 1) * 3).limit(3);
        //遍历文档数据迭代器对象
        document3.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //多条件查询(查询范围price小于90或者price大于110并且作者名字中存在a(不区分大小写)的书籍)条件外层有and
    @Test
    public void test08(){
        //新建价格小于90的查询条件
        Bson bsonlPrice = Filters.lte("price",90);
        //新建价格大于110的查询条件
        Bson bsongPrice = Filters.gte("price",110);
        //组装价格的条件
        Bson bsonPrice = Filters.or(bsonlPrice,bsongPrice);
        //模糊收索的条件（作者名字中出现a,不区分大小写）
        //设置模糊查询的参数
        Pattern pattern = Pattern.compile("a", Pattern.CASE_INSENSITIVE);
        //新建模糊查询的条件
        Bson bsonAuthor = Filters.regex("author",pattern);
        //组装价格条件和模糊查询的条件
        Bson bsonPra = Filters.and(bsonPrice,bsonAuthor);
        //执行条件查询
        FindIterable<Document> documents = mongoCollection.find(bsonPra);
        //遍历文档数据迭代器对象
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //添加单个数据
    @Test
    public void test09(){
        //新建添加的文档数据对象
        Document document = Document.parse("{'bname':'平凡的世界','author':'路遥','price':120.90,'num':390}");
        //执行添加
        mongoCollection.insertOne(document);
    }

    //添加多条数据
    @Test
    public void test10(){
        //新建添加的文档数据对象
        Document document1 = Document.parse("{'bname':'平凡的世界1','author':'路遥1','price':120.90,'num':390}");
        //新建添加的文档数据对象
        Document document2 = Document.parse("{'bname':'平凡的世界2','author':'路遥2','price':120.90,'num':390}");
        //新建添加的文档数据对象
        Document document3 = Document.parse("{'bname':'平凡的世界3','author':'路遥3','price':120.90,'num':390}");
        //执行添加多个
        mongoCollection.insertMany(Arrays.asList(document1,document2,document3));
    }

    //另一种添加单个
    @Test
    public void test11(){
        //新建添加的文档对象数据
        Document document = new Document();
        document.append("bname","雷雨");
        document.append("author","曹禺");
        document.append("price","120.80");
        document.append("num","80");
        document.append("person","四凤");
        document.append("createDate","1930/12/12");
        document.append("hander","人民出版社");
        document.append("discount","0.9");
        //执行添加
        mongoCollection.insertOne(document);
    }

    //往mongoDB中插入100000条数据
    @Test
    public void test12(){
        //得到循环开始时间
        long startTime = System.currentTimeMillis();
        for (int i=1;i<=100000;i++){
            //新建添加的文档对象数据
            Document document = new Document();
            document.append("bname","雷雨"+i);
            document.append("author","曹禺"+i);
            document.append("price","120.80");
            document.append("num","890");
            document.append("person","四凤");
            document.append("createDate","1930/12/12");
            document.append("hander","人民出版社");
            document.append("discount","0.9");
            //执行添加
            mongoCollection.insertOne(document);
        }
        //得到循环结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("共消耗"+(endTime-startTime)/1000+"s");//共消耗24s
    }

    //删除100000数据（单条件）
    @Test
    public void test13(){
        //得到循环开始时间
        long startTime = System.currentTimeMillis();
        //新建删除的条件
        Bson bson = Filters.in("person","四凤");
        //执行删除多个操作
        DeleteResult deleteResult = mongoCollection.deleteMany(bson);
        System.out.println(deleteResult);
        //得到循环结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("共消耗"+(endTime-startTime)/1000+"s");//共消耗3s
    }

    //多条件删除(根据书名和作者)
    @Test
    public void test14(){
        //新建书名的条件
        Bson bsonBname = Filters.in("bname","平凡的世界1");
        //新建作者的条件
        Bson bsonAuthor = Filters.in("author","路遥1");
        //组装删除的条件
        Bson bsonPra = Filters.and(bsonBname,bsonAuthor);
        //执行多个删除
        DeleteResult deleteResult = mongoCollection.deleteMany(bsonPra);
        System.out.println(deleteResult);
    }

    //条件删除第1条数据（根据书名和作者）
    @Test
    public void test15(){
        //新建书名的条件
        Bson bsonBname = Filters.in("bname","平凡的世界2");
        //新建作者的条件
        Bson bsonAuthor = Filters.in("author","路遥2");
        //组装删除的条件
        Bson bsonPra = Filters.and(bsonBname,bsonAuthor);
        //执行单个删除
        DeleteResult deleteResult = mongoCollection.deleteOne(bsonPra);
        System.out.println(deleteResult);
    }

    //修改100000条数据(根据数量修改价格)
    @Test
    public void test16(){
        //得到循环开始时间
        long startTime = System.currentTimeMillis();
        //新建修改的数量的条件
        Bson bsonNum = Filters.in("num","890");
        //修改的内容
        Document document = Document.parse("{$set:{\"price\":88.80}}");
        //执行多个修改
        UpdateResult updateResult = mongoCollection.updateMany(bsonNum, document);
        System.out.println(updateResult);
        //得到循环结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("共消耗"+(endTime-startTime)/1000+"s");//共消耗4s
    }

    //关闭资源
    @After
    public void closeMongoClient(){
        if(mongoClient!=null){
            mongoClient.close();
        }
    }

}
