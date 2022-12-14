package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @description: 查询月考勤VO
 * @Title: SearchMonthCheckinVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/13 9:14
 */
@Data
@ApiModel
public class SearchMonthCheckinVO {
    @NotNull
    @Range(min=2000, max=3000)
    private Integer year;

    @NotNull
    @Range(min=1, max=12)
    private Integer month;

}
