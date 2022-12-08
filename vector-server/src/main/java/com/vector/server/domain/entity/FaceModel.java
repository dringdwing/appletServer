package com.vector.server.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (FaceModel)表实体类
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_face_model")
public class FaceModel {
    //主键值@TableId
    private Long id;

    //用户ID    
    private Long userId;
    //用户人脸模型    
    private String faceModel;
}
