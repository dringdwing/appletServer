package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @Title: loginVO
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/8 21:29
 */
@Data
@ApiModel(value = "登录表单")
public class loginVO {
    @NotBlank(message="临时授权不能为空")
    private String code;
}
