package com.vector.server.entity;
import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 签到表(Checkin)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_checkin")
public class Checkin {
    //主键@TableId
    private Long id;

    //用户ID    
    private Long userId;
    //签到地址    
    private String address;
    //国家    
    private String country;
    //省份    
    private String province;
    //城市    
    private String city;
    //区划    
    private String district;
    //考勤结果    
    private Integer status;
    //风险等级    
    private Long risk;
    //签到日期    
    private Date date;
    //签到时间    
    private Date createTime;
}
