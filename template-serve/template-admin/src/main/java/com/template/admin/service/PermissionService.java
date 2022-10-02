package com.template.admin.service;

import com.template.admin.dto.PermissionCreate;
import com.template.admin.dto.PermissionTree;
import com.template.admin.dto.PermissionUpdate;
import com.template.mbg.model.SysPermission;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface PermissionService {

    SysPermission create(PermissionCreate permissionCreate);

    int delete(List<Integer> ids);

    SysPermission update(PermissionUpdate permissionUpdate);

    List<PermissionTree> list();

    List<PermissionTree> getPermissionByAdminId();
}
