package com.vector.server.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vector.server.entity.FaceModel;
import com.vector.server.service.FaceModelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (FaceModel)表控制层
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@RestController
@RequestMapping("faceModel")
public class FaceModelController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private FaceModelService faceModelService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param faceModel 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<FaceModel> page, FaceModel faceModel) {
        return success(this.faceModelService.page(page, new QueryWrapper<>(faceModel)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.faceModelService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param faceModel 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody FaceModel faceModel) {
        return success(this.faceModelService.save(faceModel));
    }

    /**
     * 修改数据
     *
     * @param faceModel 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody FaceModel faceModel) {
        return success(this.faceModelService.updateById(faceModel));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.faceModelService.removeByIds(idList));
    }
}

