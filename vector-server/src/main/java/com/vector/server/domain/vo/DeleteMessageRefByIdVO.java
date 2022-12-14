package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 删除消息
 * @Title: DeleteMessageRefByIdVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/14 12:32
 */
@ApiModel
@Data
public class DeleteMessageRefByIdVO {
    @NotBlank
    private String id;
}
