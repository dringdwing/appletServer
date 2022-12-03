package com.vector.server.entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 角色表(Role)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_role")
public class Role {
    //主键@TableId
    private Long id;

    //角色名称    
    private String roleName;
    //权限集合    
    private Object permissions;
}
