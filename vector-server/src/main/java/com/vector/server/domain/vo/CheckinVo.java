package com.vector.server.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @description:
 * @Title: CheckinVo
 * @Package com.vector.server.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/12/10 16:10
 */

@Data
@ApiModel
public class CheckinVo {
    private String address;
    private String country;
    private String province;
    private String city;
    private String district;
}