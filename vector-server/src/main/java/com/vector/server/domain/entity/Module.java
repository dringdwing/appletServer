package com.vector.server.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 模块资源表(Module)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_module")
public class Module {
    //主键@TableId
    private Long id;

    //模块编号    
    private String moduleCode;
    //模块名称    
    private String moduleName;
}
