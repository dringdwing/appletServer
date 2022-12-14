package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 把未读消息更新成已读消息
 * @Title: UpdateUnreadMessageVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/14 12:29
 */
@ApiModel
@Data
public class UpdateUnreadMessageVO {
    @NotBlank(message = "消息id不能为空")
    private String id;
}

