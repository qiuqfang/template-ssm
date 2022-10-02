package com.template.admin.component;

import cn.hutool.core.collection.CollectionUtil;
import com.template.admin.bo.SysAdminDetail;
import com.template.common.api.ResultCode;
import com.template.security.exception.ApiException;
import com.template.security.util.JwtTokenUtil;
import com.template.mbg.mapper.SysAdminMapper;
import com.template.mbg.mapper.SysRoleMapper;
import com.template.mbg.model.*;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class JwtTokenRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenRealm.class);
    private static final String SEPARATOR = ",";

    private JwtTokenUtil jwtTokenUtil;
    private SysAdminMapper adminMapper;
    private SysRoleMapper roleMapper;

    public JwtTokenRealm() {
    }

    @Autowired
    public JwtTokenRealm(JwtTokenUtil jwtTokenUtil, SysAdminMapper adminMapper, SysRoleMapper roleMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.adminMapper = adminMapper;
        this.roleMapper = roleMapper;
    }

    /**
     * 授权操作
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOGGER.info("doGetAuthorizationInfo");
        SysAdminDetail userDetail = (SysAdminDetail) principalCollection.getPrimaryPrincipal();
        List<String> roles = userDetail.getRoles();
        List<String> permissions = userDetail.getPermissions();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }

    /**
     * 认证操作
     *
     * @param authenticationToken
     * @return
     * @throws ApiException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws ApiException {
        LOGGER.info("doGetAuthenticationInfo");
        // 获取token
        String token = (String) authenticationToken.getCredentials();
        // 从token中获取用户名
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            throw new ApiException(ResultCode.AUTHENTICATE_FAILED);
        }

        SysAdminExample adminExample = new SysAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username).andTokenEqualTo(token);
        adminMapper.selectByExample(adminExample);
        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);

        if (CollectionUtil.isEmpty(admins) || !jwtTokenUtil.validateToken(token, username)) {
            throw new ApiException(ResultCode.AUTHENTICATE_FAILED);
        }

        SysAdmin admin = admins.get(0);
        SysAdminDetail adminDetail = generatorAdminDetail(admin);

        return new SimpleAuthenticationInfo(adminDetail, token, getName());
    }

    private SysAdminDetail generatorAdminDetail(SysAdmin admin) {
        SysAdminDetail adminDetail = new SysAdminDetail();
        BeanUtils.copyProperties(admin, adminDetail);
        if ("".equals(admin.getRoles())) {
            return adminDetail;
        }

        // 获取用户拥有的所有角色
        List<Integer> roleIds = new ArrayList<>();
        SysRoleExample roleExample = new SysRoleExample();
        for (String roleId : admin.getRoles().split(SEPARATOR)) {
            roleIds.add(Integer.valueOf(roleId));
        }
        roleExample.createCriteria().andIdIn(roleIds);
        List<SysRole> roles = roleMapper.selectByExample(roleExample);

        // 获取用户角色拥有的所有权限和所有菜单
        List<String> permissionsId = new ArrayList<>();
        List<String> menusId = new ArrayList<>();
        for (SysRole role : roles) {
            if (!"".equals(role.getPermissions())) {
                // TODO:没有去重
                permissionsId.addAll(Arrays.asList(role.getPermissions().split(SEPARATOR)));
            }
            if (!"".equals(role.getMenus())) {
                // TODO:没有去重
                menusId.addAll(Arrays.asList(role.getMenus().split(SEPARATOR)));
            }
        }

        // 保存用户拥有的角色ID
        adminDetail.setRoles(Arrays.asList(admin.getRoles().split(SEPARATOR)));
        // 保存用户用户的权限ID
        adminDetail.setPermissions(permissionsId);
        // 保存用户用户的菜单ID
        adminDetail.setMenus(menusId);
        return adminDetail;
    }
}
