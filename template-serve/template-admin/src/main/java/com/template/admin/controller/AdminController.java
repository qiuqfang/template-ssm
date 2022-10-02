package com.template.admin.controller;

import com.template.admin.dto.AdminCreate;
import com.template.admin.dto.AdminDelete;
import com.template.admin.dto.AdminUpdate;
import com.template.admin.service.AdminService;
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
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequiresPermissions(value = {"2", "*"}, logical = Logical.OR)
    @PostMapping("create")
    public CommonResult<?> create(@RequestBody @Validated AdminCreate userCreate, BindingResult bindingResult) {
        int count = adminService.create(userCreate);
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"3", "*"}, logical = Logical.OR)
    @PostMapping("delete")
    public CommonResult<?> delete(@RequestBody @Validated AdminDelete adminDelete, BindingResult bindingResult) {
        int count = adminService.delete(adminDelete.getIds());
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"4", "*"}, logical = Logical.OR)
    @PostMapping("update")
    public CommonResult<?> update(@RequestBody @Validated AdminUpdate adminUpdate, BindingResult bindingResult) {
        int count = adminService.update(adminUpdate.getId(), adminUpdate);
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success(count);
    }

    @RequiresPermissions(value = {"5", "*"}, logical = Logical.OR)
    @GetMapping("list")
    public CommonResult<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "") String keyword) {
        return CommonResult.success(adminService.list(pageNum, pageSize, keyword));
    }

}
