package cn.com.java.admin.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author djin
 *    AdminUserAuthority实体类
 * @date 2019-12-07 11:25:44
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class AdminUserAuthorityEntity implements Serializable{

	  private static final long serialVersionUID = 1L;
	
      //后台用户主键
	  private Long userid;
      //后台菜单主键
	  private Long menuid;

	  /**
	   * 设置：后台用户主键
	   */
	  public void setUserid(Long userid) {
		  this.userid = userid;
	  }
	  /**
	   * 获取：后台用户主键
	   */
	  public Long getUserid() {
	   	  return userid;
	  }
	  /**
	   * 设置：后台菜单主键
	   */
	  public void setMenuid(Long menuid) {
		  this.menuid = menuid;
	  }
	  /**
	   * 获取：后台菜单主键
	   */
	  public Long getMenuid() {
	   	  return menuid;
	  }

	 
	  @Override
	  public String toString() {
		  return  ReflectionToStringBuilder.toString(this);
	  }

}
