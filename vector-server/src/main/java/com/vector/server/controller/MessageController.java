package com.vector.server.controller;

import com.vector.server.config.shiro.JwtUtil;
import com.vector.server.domain.vo.SearchMessageByIdVO;
import com.vector.server.domain.vo.SearchMessageByPageVO;
import com.vector.server.domain.vo.UpdateUnreadMessageVO;
import com.vector.server.service.MessageService;
import com.vector.server.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @Title: MessageController
 * @Package com.vector.server.controller
 * @Author 芝士汉堡
 * @Date 2022/12/14 12:18
 */
@RestController
@RequestMapping("/message")
@Api(tags = "消息模块接口")
public class MessageController {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private
    MessageService messageservice;

    @GetMapping("/searchMessageByPage")
    @ApiOperation(value = "获取分页消息列表")
    public R searchMessageByPage(@Valid @RequestBody SearchMessageByPageVO searchMessageByPageVO, @RequestHeader String token) {
        int userId = jwtUtil.getUserId(token);
        /*计算分页偏移量*/
        int offset = (searchMessageByPageVO.getPage() - 1) * searchMessageByPageVO.getLength();
        List<HashMap> list = messageservice.searchMessageByPage(userId, offset, searchMessageByPageVO.getLength());
        return R.ok().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @ApiOperation("根据ID查询消息")
    public R searchMessageById(@Valid @RequestBody SearchMessageByIdVO searchMessageByIdVO) {
        HashMap map = messageservice.searchMessageById(searchMessageByIdVO.getId());
        return R.ok().put("result", map);
    }

    @PostMapping("/updateUnreadMessage")
    @ApiOperation("未读消息更新成已读消息")
    public R updateUnreadMessage(@Valid @RequestBody UpdateUnreadMessageVO updateUnreadMessageVO) {
        long rows = messageservice.updateUnreadMessage(updateUnreadMessageVO.getId());
        return R.ok().put("result", rows == 1 ? true : false);
    }

    @PostMapping("/deleteMessageRefById")
    @ApiOperation("删除消息")
    public R deleteMessage(@Valid @RequestBody SearchMessageByIdVO searchMessageByIdVO) {
        long rows = messageservice.deleteMessageRefById(searchMessageByIdVO.getId());
        return R.ok().put("result", rows == 1 ? true : false);
    }

}
