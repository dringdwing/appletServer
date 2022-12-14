package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description: 获取分页消息列表
 * @Title: SearchMessageByPageVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/14 12:15
 */
@ApiModel
@Data
public class SearchMessageByPageVO {

    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    @Range(min = 1, max = 40)
    private Integer length;

}
