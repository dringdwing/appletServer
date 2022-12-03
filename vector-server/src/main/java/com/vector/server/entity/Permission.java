package com.vector.server.entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_permission")
public class Permission {
    //主键@TableId
    private Long id;

    //权限    
    private String permissionName;
    //模块ID    
    private Long moduleId;
    //行为ID    
    private Long actionId;
}
