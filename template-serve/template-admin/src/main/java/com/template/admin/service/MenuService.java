package com.template.admin.service;

import com.template.admin.dto.*;
import com.template.mbg.model.SysMenu;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface MenuService {

    SysMenu create(MenuCreate menuCreate);

    int delete(List<Integer> ids);

    SysMenu update(MenuUpdate menuUpdate);

    List<MenuTree> list();

    List<MenuTree> getMenuByAdminId();
}
