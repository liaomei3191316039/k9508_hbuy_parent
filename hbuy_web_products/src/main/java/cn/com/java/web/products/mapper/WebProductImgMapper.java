package cn.com.java.web.products.mapper;

import cn.com.java.model.WebProductImgEntity;
import org.apache.ibatis.annotations.Select;
import org.hibernate.validator.constraints.EAN;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author djin
 *    WebProductImgMapper层
 * @date 2019-12-09 10:27:01
 */
@Repository
public interface WebProductImgMapper extends BaseMapper<WebProductImgEntity> {

    //根据商品id查询商品轮播图路径
    @Select("select imgUrl from web_product_img where productId=#{productId}")
    List<String> selWebImgUrlByProId(Long productId) throws Exception;
}
