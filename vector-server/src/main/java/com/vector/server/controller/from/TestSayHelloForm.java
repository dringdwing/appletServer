package com.vector.server.controller.from;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class TestSayHelloForm{
    @ApiModelProperty("姓名")private String name;
}