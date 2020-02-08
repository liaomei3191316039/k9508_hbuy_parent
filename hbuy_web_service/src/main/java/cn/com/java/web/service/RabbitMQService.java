package cn.com.java.web.service;

/**
 *   RabbitMQ的业务层接口
 */
public interface RabbitMQService {

    void addRabbitMQToExC(Long secId, Long proId, Float secPrice, Long uid) throws Exception;

    void addRabbitMQToBuyCar(String proIds, Float allPrice, Long uid) throws Exception;
}
