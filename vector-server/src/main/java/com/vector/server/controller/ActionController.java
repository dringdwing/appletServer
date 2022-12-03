package com.vector.server.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vector.server.entity.Action;
import com.vector.server.service.ActionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 行为表(Action)表控制层
 *
 * @author makejava
 * @since 2022-12-03 20:55:38
 */
@RestController
@RequestMapping("action")
public class ActionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ActionService actionService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param action 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Action> page, Action action) {
        return success(this.actionService.page(page, new QueryWrapper<>(action)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.actionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param action 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Action action) {
        return success(this.actionService.save(action));
    }

    /**
     * 修改数据
     *
     * @param action 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Action action) {
        return success(this.actionService.updateById(action));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.actionService.removeByIds(idList));
    }
}

