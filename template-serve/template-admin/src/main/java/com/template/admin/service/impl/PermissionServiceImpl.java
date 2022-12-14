package com.template.admin.service.impl;

import com.template.admin.bo.SysAdminDetail;
import com.template.admin.dto.PermissionCreate;
import com.template.admin.dto.PermissionTree;
import com.template.admin.dto.PermissionUpdate;
import com.template.admin.service.PermissionService;
import com.template.mbg.mapper.SysPermissionMapper;
import com.template.mbg.mapper.SysRoleMapper;
import com.template.mbg.model.SysPermission;
import com.template.mbg.model.SysPermissionExample;
import com.template.mbg.model.SysRole;
import com.template.security.exception.ApiException;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final SysRoleMapper roleMapper;
    private final SysPermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(SysRoleMapper roleMapper, SysPermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public SysPermission create(PermissionCreate permissionCreate) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionCreate, permission);
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        int count = permissionMapper.insertSelective(permission);
        if (count == 1) {
            return permission;
        }
        return null;
    }

    @Override
    public int delete(List<Integer> ids) {
        int count = 0;
        // ?????????id???????????????????????????????????????????????????????????????
        SysPermissionExample permissionExample = new SysPermissionExample();
        permissionExample.createCriteria().andPidIn(ids);
        List<SysPermission> permissions = permissionMapper.selectByExample(permissionExample);
        if (!CollectionUtils.isEmpty(permissions)) {
            throw new ApiException("?????????????????????");
        }
        SysPermissionExample permissionExample1 = new SysPermissionExample();
        permissionExample1.createCriteria().andIdIn(ids);
        count = permissionMapper.deleteByExample(permissionExample1);
        return count;
    }

    @Override
    public SysPermission update(PermissionUpdate permissionUpdate) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionUpdate, permission);
        int count = permissionMapper.updateByPrimaryKeySelective(permission);
        if (count == 1) {
            return permission;
        }
        return null;
    }

    @Override
    public List<PermissionTree> list() {
        List<SysPermission> permissions = permissionMapper.selectByExample(null);
        return generatePermissionTree(permissions, (SysRole) null);
    }

    @Override
    public List<PermissionTree> getPermissionByAdminId() {
        List<SysPermission> permissions = permissionMapper.selectByExample(null);
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        List<String> permissionsId = adminDetail.getPermissions();

        return generatePermissionTree(permissions, permissionsId);
    }

    private List<PermissionTree> generatePermissionTree(List<SysPermission> permissions, SysRole role) {

        // ??????map??????????????????,?????????????????????.????????????????????? ,????????????.
        Map<Integer, PermissionTree> map = new HashMap<>();
        if (role != null) {
            String permissionsId = role.getPermissions();
            for (SysPermission permission : permissions) {
                PermissionTree permissionTree = new PermissionTree();
                BeanUtils.copyProperties(permission, permissionTree);
                // ?????????????????????????????????????????????
                if (!StringUtils.isEmpty(permissionsId) && permissionsId.contains(permission.getId().toString())) {
                    permissionTree.setStatus(Boolean.TRUE);
                }
                if ("*".equals(permissionsId)) {
                    permissionTree.setStatus(Boolean.TRUE);
                }
                map.put(permission.getId(), permissionTree);
            }
        } else {
            for (SysPermission permission : permissions) {
                PermissionTree permissionTree = new PermissionTree();
                BeanUtils.copyProperties(permission, permissionTree);
                permissionTree.setStatus(Boolean.TRUE);
                map.put(permission.getId(), permissionTree);
            }
        }

        // ???????????????
        return getPermissionTrees(permissions, map);
    }

    private List<PermissionTree> generatePermissionTree(List<SysPermission> permissions, List<String> permissionsId) {
        // ??????map??????????????????,?????????????????????.????????????????????? ,????????????.
        Map<Integer, PermissionTree> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(permissionsId)) {
            for (SysPermission permission : permissions) {
                PermissionTree permissionTree = new PermissionTree();
                BeanUtils.copyProperties(permission, permissionTree);
                // ?????????????????????????????????????????????
                if (permissionsId.contains(permission.getId().toString()) || permissionsId.contains("*")) {
                    permissionTree.setStatus(Boolean.TRUE);
                }
                map.put(permission.getId(), permissionTree);
            }
        } else {
            for (SysPermission permission : permissions) {
                PermissionTree permissionTree = new PermissionTree();
                BeanUtils.copyProperties(permission, permissionTree);
                permissionTree.setStatus(Boolean.TRUE);
                map.put(permission.getId(), permissionTree);
            }
        }

        // ???????????????
        return getPermissionTrees(permissions, map);
    }

    private List<PermissionTree> getPermissionTrees(List<SysPermission> permissions, Map<Integer, PermissionTree> map) {
        List<PermissionTree> root = new ArrayList<>();
        for (SysPermission permission : permissions) {
            // ????????????????????????????????????
            if (permission.getPid() == null) {
                root.add(map.get(permission.getId()));
            } else {
                PermissionTree permissionTree = map.get(permission.getPid());
                permissionTree.getChildren().add(map.get(permission.getId()));
            }
        }
        LOGGER.info("????????????{}", root);
        return root;
    }
}
