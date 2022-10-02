package com.template.admin.controller;

import com.template.admin.dto.*;
import com.template.admin.service.PermissionService;
import com.template.common.api.CommonResult;
import com.template.mbg.model.SysPermission;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@RestController
@RequestMapping("permission")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequiresPermissions(value = {"7", "*"}, logical = Logical.OR)
    @PostMapping("create")
    public CommonResult<?> create(@RequestBody @Validated PermissionCreate permissionCreate, BindingResult bindingResult) {
        SysPermission permission = permissionService.create(permissionCreate);
        if (permission == null) {
            return CommonResult.failed("添加失败");
        }
        return CommonResult.success(permission, "添加成功");
    }

    @RequiresPermissions(value = {"8", "*"}, logical = Logical.OR)
    @PostMapping("delete")
    public CommonResult<?> delete(@RequestBody @Validated PermissionDelete permissionDelete, BindingResult bindingResult) {
        int count = permissionService.delete(permissionDelete.getIds());
        if (count == 0) {
            return CommonResult.failed("删除失败");
        }
        return CommonResult.success("删除成功");
    }

    @RequiresPermissions(value = {"9", "*"}, logical = Logical.OR)
    @PostMapping("update")
    public CommonResult<?> update(@RequestBody @Validated PermissionUpdate permissionUpdate, BindingResult bindingResult) {
        SysPermission permission = permissionService.update(permissionUpdate);
        if (permission == null) {
            return CommonResult.failed("更新失败");
        }
        return CommonResult.success(permission, "更新成功");
    }

    @RequiresPermissions(value = {"10", "*"}, logical = Logical.OR)
    @GetMapping("list")
    public CommonResult<?> list() {
        return CommonResult.success(permissionService.list());
    }

    @RequiresPermissions(value = {"21", "*"}, logical = Logical.OR)
    @GetMapping("list/by/adminId")
    public CommonResult<?> getPermissionByAdminId() {
        List<PermissionTree> list = permissionService.getPermissionByAdminId();
        return CommonResult.success(list);
    }
}
