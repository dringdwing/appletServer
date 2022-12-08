package com.vector.server.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Config)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:16
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_config")
public class Config {
    //主键@TableId
    private Long id;

    //参数名    
    private String paramKey;
    //参数值    
    private String paramValue;
    //状态    
    private Integer status;
    //备注    
    private String remark;
}
