package com.vector.server.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.User;
import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.exception.AppleServerException;
import com.vector.server.mapper.UserMapper;
import com.vector.server.service.DeptService;
import com.vector.server.service.UserService;
import com.vector.server.task.MessageTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Resource
    private DeptService deptService;

    @Resource
    private MessageTask messageTask;

    /**
     * 获取OpenId
     *
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登录凭证错误");
        }
        return openId;
    }


    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        if (registerCode.equals("000000")) {
            boolean bool = baseMapper.selectCount(new QueryWrapper<User>()
                    .select("root")
                    .eq("root", 1)) == 1 ? true : false;
            if (!bool) {
//                Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>()
//                        .select("id")
//                        .eq(deptName!=null,"dept_name", "value"));
                String openId = getOpenId(code);
                User user = new User();
                user.setOpenId(openId);
                user.setNickname(nickname);
                user.setPhoto(photo);
                user.setRole("[0]");
                user.setRoot(1);
                user.setStatus(1);
                user.setCreateTime(new Date());
                baseMapper.insert(user);
                User userId = baseMapper.selectOne(new QueryWrapper<User>()
                        .eq("open_id", openId)
                        .eq("status", 1));


                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setSenderId(0);
                messageEntity.setSenderName("系统消息");
                messageEntity.setUuid(IdUtil.simpleUUID());
                messageEntity.setMsg("欢迎您，" + nickname + "，您已成功注册为管理员！");
                messageEntity.setSendTime(new Date());
                messageTask.sendAsync(userId.getId() + "", messageEntity);


                return userId.getId().intValue();
            }


        } else {
            throw new AppleServerException("无法绑定管理员账号");
        }
        return 0;
    }

    /**
     * 根据用户ID查询用户权限
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions = baseMapper.searchUserPermissions(userId);
        return permissions;
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        User user = baseMapper.selectOne(new QueryWrapper<User>()
                .eq("open_id", openId)
                .eq("status", 1));
        if (user == null) {
            throw new AppleServerException("用户不存在");
        }
        //  从消息队列中接收消息，转移到消息表
        messageTask.receiveAsync(user.getId() + "");
        return user.getId().intValue();
    }

    /**
     * 查询入职时间 作为签到起始天数
     *
     * @param userId
     * @return
     */
    @Override
    public String searchUserHiredate(int userId) {
        String hiredate = String.valueOf(baseMapper
                .selectOne(new QueryWrapper<User>()
                        .select("hiredate")
                        .eq("id", userId)
                        .eq("status", 1)).getHiredate());
        return hiredate;
    }
}