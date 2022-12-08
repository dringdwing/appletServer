package com.vector.server.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 疫情城市列表(City)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_city")
public class City {
    //主键@TableId
    private Long id;

    //城市名称    
    private String city;
    //拼音简称    
    private String code;
}
