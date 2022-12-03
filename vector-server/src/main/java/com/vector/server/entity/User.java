package com.vector.server.entity;
import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class User {
    //主键@TableId
    private Long id;

    //长期授权字符串    
    private String openId;
    //昵称    
    private String nickname;
    //头像网址    
    private String photo;
    //姓名    
    private String name;
    //性别    
    private Object sex;
    //手机号码    
    private String tel;
    //邮箱    
    private String email;
    //入职日期    
    private Date hiredate;
    //角色    
    private Object role;
    //是否是超级管理员    
    private Integer root;
    //部门编号    
    private Long deptId;
    //状态    
    private Integer status;
    //创建时间    
    private Date createTime;
}
