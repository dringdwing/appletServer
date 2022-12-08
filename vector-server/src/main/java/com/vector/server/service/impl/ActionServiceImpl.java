package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Action;
import com.vector.server.mapper.ActionMapper;
import com.vector.server.service.ActionService;
import org.springframework.stereotype.Service;
/**
 * 行为表(Action)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("actionService")
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements ActionService {
}
