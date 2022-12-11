package com.vector.server.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @Title: SystemConstants
 * @Package com.vector.server.constants
 * @Author 芝士汉堡
 * @Date 2022/12/9 9:51
 */

@Data
@Component
public class SystemConstants {
    public String attendanceStartTime;
    public String attendanceTime;
    public String attendanceEndTime;
    public String closingStartTime;
    public String closingTime;
    public String closingEndTime;
}
