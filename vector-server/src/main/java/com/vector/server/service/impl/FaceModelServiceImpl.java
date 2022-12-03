package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.entity.FaceModel;
import com.vector.server.mapper.FaceModelMapper;
import com.vector.server.service.FaceModelService;
import org.springframework.stereotype.Service;
/**
 * (FaceModel)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("faceModelService")
public class FaceModelServiceImpl extends ServiceImpl<FaceModelMapper, FaceModel> implements FaceModelService {
}
