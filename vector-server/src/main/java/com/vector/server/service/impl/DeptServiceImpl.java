package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.entity.Dept;
import com.vector.server.mapper.DeptMapper;
import com.vector.server.service.DeptService;
import org.springframework.stereotype.Service;
/**
 * (Dept)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
}
