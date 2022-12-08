package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description: 接收前端注册表单的数据
 * @Title: RegisterVo
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/8 17:19
 */
@Data
@ApiModel(value = "注册表单")
public class RegisterVo {
    @NotBlank(message = "注册码不能为空")
    @Pattern(regexp = "^[0-9]{6}$",message = "注册码格式不正确")
    private String registerCode;
    @NotBlank(message = "微信临时授权不能为空")
    private String code;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotBlank(message = "头像不能为空")
    private String photo;
}
