package com.vector.server.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vector.server.entity.Config;
import com.vector.server.service.ConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Config)表控制层
 *
 * @author makejava
 * @since 2022-12-03 20:55:14
 */
@RestController
@RequestMapping("config")
public class ConfigController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ConfigService configService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param config 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Config> page, Config config) {
        return success(this.configService.page(page, new QueryWrapper<>(config)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.configService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param config 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Config config) {
        return success(this.configService.save(config));
    }

    /**
     * 修改数据
     *
     * @param config 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Config config) {
        return success(this.configService.updateById(config));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.configService.removeByIds(idList));
    }
}

