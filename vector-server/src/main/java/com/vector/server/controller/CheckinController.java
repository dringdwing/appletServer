package com.vector.server.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vector.server.entity.Checkin;
import com.vector.server.service.CheckinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 签到表(Checkin)表控制层
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@RestController
@RequestMapping("checkin")
public class CheckinController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CheckinService checkinService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param checkin 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Checkin> page, Checkin checkin) {
        return success(this.checkinService.page(page, new QueryWrapper<>(checkin)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.checkinService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param checkin 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Checkin checkin) {
        return success(this.checkinService.save(checkin));
    }

    /**
     * 修改数据
     *
     * @param checkin 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Checkin checkin) {
        return success(this.checkinService.updateById(checkin));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.checkinService.removeByIds(idList));
    }
}

