package com.vector.server.controller;

import com.vector.server.controller.from.TestSayHelloForm;
import com.vector.server.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description:
 * @Title: TestController
 * @Package com.vector.server.controller
 * @Author 芝士汉堡
 * @Date 2022/12/4 6:53
 */

@RestController
@RequestMapping("/test")
@Api("测试Web接口")
public class TestController {

    @PostMapping("/sayHello")
    @ApiOperation("最简单的测试方法")
    public R sayHello(@Valid @RequestBody TestSayHelloForm form){
        return R.ok().put("message", "Hello"+form.getName());
    }
}