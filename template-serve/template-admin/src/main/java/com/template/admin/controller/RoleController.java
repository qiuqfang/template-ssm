package com.template.admin.controller;

import afu.org.checkerframework.checker.igj.qual.I;
import com.template.admin.dto.PermissionDelete;
import com.template.admin.dto.RoleCreate;
import com.template.admin.dto.RoleDelete;
import com.template.admin.dto.RoleUpdate;
import com.template.admin.service.RoleService;
import com.template.common.api.CommonResult;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequiresPermissions(value = {"7", "*"}, logical = Logical.OR)
    @PostMapping("create")
    public CommonResult<?> create(@RequestBody @Validated RoleCreate roleCreate, BindingResult bindingResult) {
        int count = roleService.create(roleCreate);
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"8", "*"}, logical = Logical.OR)
    @PostMapping("delete")
    public CommonResult<?> delete(@RequestBody @Validated RoleDelete roleDelete, BindingResult bindingResult) {
        int count = roleService.delete(roleDelete.getIds());
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"9", "*"}, logical = Logical.OR)
    @PostMapping("update")
    public CommonResult<?> update(@RequestBody @Validated RoleUpdate roleUpdate,
                                  BindingResult bindingResult) {
        int count = roleService.update(roleUpdate.getId(), roleUpdate);
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"10", "*"}, logical = Logical.OR)
    @GetMapping("list")
    public CommonResult<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "") String keyword) {
        return CommonResult.success(roleService.list(pageNum, pageSize, keyword));
    }

    @GetMapping("all/by/adminId")
    public CommonResult<?> getRoleByAdminId() {
        return CommonResult.success(roleService.getRoleByAdminId());
    }
}
