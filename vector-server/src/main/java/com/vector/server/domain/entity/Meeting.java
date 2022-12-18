package com.vector.server.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 会议表(Meeting)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_meeting")
public class Meeting {
    //主键@TableId
    private String id;

    //UUID    
    private String uuid;
    //会议题目    
    private String title;
    //创建人ID    
    private Long creatorId;
    //日期    
    private String date;
    //开会地点    
    private String place;
    //开始时间    
    private String start;
    //结束时间    
    private String end;
    //会议类型（1在线会议，2线下会议）    
    private Integer type;
    //参与者    
    private Object members;
    //会议内容    
    private String desc;
    //工作流实例ID    
    private String instanceId;
    //状态（1未开始，2进行中，3已结束）    
    private Integer status;
    //创建时间    
    private Date createTime;
}
