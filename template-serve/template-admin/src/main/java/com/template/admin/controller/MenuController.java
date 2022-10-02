package com.template.admin.controller;

import com.template.admin.dto.*;
import com.template.admin.service.MenuService;
import com.template.common.api.CommonResult;
import com.template.mbg.model.SysMenu;
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
@RequestMapping("menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequiresPermissions(value = {"17", "*"}, logical = Logical.OR)
    @PostMapping("create")
    public CommonResult<?> create(@RequestBody @Validated MenuCreate menuCreate, BindingResult bindingResult) {
        SysMenu menu = menuService.create(menuCreate);
        if (menu == null) {
            return CommonResult.failed("添加失败");
        }
        return CommonResult.success(menu, "添加成功");
    }

    @RequiresPermissions(value = {"18", "*"}, logical = Logical.OR)
    @PostMapping("delete")
    public CommonResult<?> delete(@RequestBody @Validated MenuDelete menuDelete, BindingResult bindingResult) {
        int count = menuService.delete(menuDelete.getIds());
        if (count == 0) {
            return CommonResult.failed("删除失败");
        }
        return CommonResult.success("删除成功");
    }

    @RequiresPermissions(value = {"19", "*"}, logical = Logical.OR)
    @PostMapping("update")
    public CommonResult<?> update(@RequestBody @Validated MenuUpdate menuUpdate, BindingResult bindingResult) {
        SysMenu menu = menuService.update(menuUpdate);
        if (menu == null) {
            return CommonResult.failed("更新失败");
        }
        return CommonResult.success(menu, "更新成功");
    }

    @RequiresPermissions(value = {"20", "*"}, logical = Logical.OR)
    @GetMapping("list")
    public CommonResult<?> list() {
        return CommonResult.success(menuService.list(), "查询成功");
    }

    @RequiresPermissions(value = {"22", "*"}, logical = Logical.OR)
    @GetMapping("list/by/adminId")
    public CommonResult<?> getMenuByAdminId() {
        List<MenuTree> list = menuService.getMenuByAdminId();
        return CommonResult.success(list);
    }
}
