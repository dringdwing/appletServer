package com.vector.server.entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 行为表(Action)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:38
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_action")
public class Action {
    //主键@TableId
    private Long id;

    //行为编号    
    private String actionCode;
    //行为名称    
    private String actionName;
}
