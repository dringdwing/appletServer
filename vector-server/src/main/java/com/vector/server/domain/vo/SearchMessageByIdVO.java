package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 根据ID查询消息
 * @Title: SearchMessageByIdVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/14 12:26
 */
@ApiModel
@Data
public class SearchMessageByIdVO {
    @NotBlank(message = "消息id不能为空")
    private String id;
}