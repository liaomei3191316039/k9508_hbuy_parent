package cn.com.java.web.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author djin
 *    WebOrder实体类
 * @date 2019-12-09 10:27:01
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WebOrderEntity implements Serializable{

	  private static final long serialVersionUID = 1L;
	
      //主键
	  private Integer id;
      //订单编号
	  private String orderno;
      //用户ID
	  private Long userid;
      //0已经创建 1未支付  2已经支付  3已经收货  4已经评价
	  private String orderstatus;
      //金额
	  private Float cost;

	  /**
	   * 设置：主键
	   */
	  public void setId(Integer id) {
		  this.id = id;
	  }
	  /**
	   * 获取：主键
	   */
	  public Integer getId() {
	   	  return id;
	  }
	  /**
	   * 设置：订单编号
	   */
	  public void setOrderno(String orderno) {
		  this.orderno = orderno;
	  }
	  /**
	   * 获取：订单编号
	   */
	  public String getOrderno() {
	   	  return orderno;
	  }
	  /**
	   * 设置：用户ID
	   */
	  public void setUserid(Long userid) {
		  this.userid = userid;
	  }
	  /**
	   * 获取：用户ID
	   */
	  public Long getUserid() {
	   	  return userid;
	  }
	  /**
	   * 设置：0已经创建 1未支付  2已经支付  3已经收货  4已经评价
	   */
	  public void setOrderstatus(String orderstatus) {
		  this.orderstatus = orderstatus;
	  }
	  /**
	   * 获取：0已经创建 1未支付  2已经支付  3已经收货  4已经评价
	   */
	  public String getOrderstatus() {
	   	  return orderstatus;
	  }
	  /**
	   * 设置：金额
	   */
	  public void setCost(Float cost) {
		  this.cost = cost;
	  }
	  /**
	   * 获取：金额
	   */
	  public Float getCost() {
	   	  return cost;
	  }

	 
	  @Override
	  public String toString() {
		  return  ReflectionToStringBuilder.toString(this);
	  }

}
