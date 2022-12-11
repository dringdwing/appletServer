package com.vector.server.config.shiro;

import com.vector.server.domain.entity.User;
import com.vector.server.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @description: OAuth2 Realm类是AuthorizingRealm的实现类，要在这个实现类中定义认证和授权的方法。
 * 因为认证与授权模块设计到用户模块和权限模块，现在还没有真正的开发业务模块，所以这里先暂时定义空的认证去授权方法，
 * 把Shiro和JWT整合起来，在后续章节再实现认证与授权。
 * @Title: OAuth2Realm
 * @Package com.vector.server.config.shiro
 * @Author 芝士汉堡
 * @Date 2022/12/4 11:59
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    /**
     * 判断是否支持当前token 是否为OAuth2Token类型的
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权（验证权限的时候调用）
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        Long id = user.getId();
        // 查询用户的权限列表
        Set<String> permsSet = userService.searchUserPermissions(id.intValue());
        // 把权限列表添加到info对象中
        SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;

    }

    /**
     * 认证（登录时调用）
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        int userId = jwtUtil.getUserId(accessToken);
        //  从令牌中获取userId,然后检测该账户是否被冻结。
        User user = userService.getBaseMapper().selectById(userId) ;
        /*用户离职*/
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被冻结,请联系管理员");
        }
        //  往info对象中添加用户信息、Token字符串、当前Realm的名称
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
