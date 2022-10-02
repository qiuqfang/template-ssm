package com.template.admin.service.impl;

import com.template.admin.bo.SysAdminDetail;
import com.template.admin.dto.*;
import com.template.admin.service.MenuService;
import com.template.mbg.mapper.SysMenuMapper;
import com.template.mbg.model.*;
import com.template.mbg.model.SysMenu;
import com.template.security.exception.ApiException;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final SysMenuMapper menuMapper;

    @Autowired
    public MenuServiceImpl(SysMenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public SysMenu create(MenuCreate menuCreate) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuCreate, menu);
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        int count = menuMapper.insertSelective(menu);
        if (count == 1) {
            return menu;
        }
        return null;
    }

    @Override
    public int delete(List<Integer> ids) {
        int count = 0;
        SysMenuExample menuExample = new SysMenuExample();
        menuExample.createCriteria().andPidIn(ids);
        List<SysMenu> menus = menuMapper.selectByExample(menuExample);
        if (!CollectionUtils.isEmpty(menus)) {
            throw new ApiException("请确保无子菜单");
        }
        SysMenuExample menuExample1 = new SysMenuExample();
        menuExample1.createCriteria().andIdIn(ids);
        count = menuMapper.deleteByExample(menuExample1);
        return count;
    }

    @Override
    public SysMenu update(MenuUpdate menuUpdate) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuUpdate, menu);
        int count = menuMapper.updateByPrimaryKeySelective(menu);
        if (count == 1) {
            return menu;
        }
        return null;
    }

    @Override
    public List<MenuTree> list() {
        List<SysMenu> menus = menuMapper.selectByExample(null);
        return generateMenuTree(menus, (SysRole) null);
    }

    @Override
    public List<MenuTree> getMenuByAdminId() {
        List<SysMenu> menus = menuMapper.selectByExample(null);
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        List<String> menusId = adminDetail.getMenus();

        return generateMenuTree(menus, menusId);
    }

    private List<MenuTree> generateMenuTree(List<SysMenu> menus, SysRole role) {

        // 通过map集合来查找父,来组合父子关系.减少循环的次数 ,提高性能.
        Map<Integer, MenuTree> map = new HashMap<>();
        if (role != null) {
            String menusId = role.getMenus();
            for (SysMenu menu : menus) {
                MenuTree menuTree = new MenuTree();
                BeanUtils.copyProperties(menu, menuTree);
                // 确定角色是否拥有哪些权限
                if (!StringUtils.isEmpty(menusId) && menusId.contains(menu.getId().toString())) {
                    menuTree.setStatus(Boolean.TRUE);
                }
                if ("*".equals(menusId)) {
                    menuTree.setStatus(Boolean.TRUE);
                }
                map.put(menu.getId(), menuTree);
            }
        } else {
            for (SysMenu menu : menus) {
                MenuTree menuTree = new MenuTree();
                BeanUtils.copyProperties(menu, menuTree);
                map.put(menu.getId(), menuTree);
            }
        }

        // 生成权限树
        return getMenuTrees(menus, map);
    }

    private List<MenuTree> generateMenuTree(List<SysMenu> menus, List<String> menusId) {

        // 通过map集合来查找父,来组合父子关系.减少循环的次数 ,提高性能.
        Map<Integer, MenuTree> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(menusId)) {
            for (SysMenu menu : menus) {
                MenuTree menuTree = new MenuTree();
                BeanUtils.copyProperties(menu, menuTree);
                // 确定当前登录用户是否拥有该权限
                if (menusId.contains(menu.getId().toString()) || menusId.contains("*")) {
                    menuTree.setStatus(Boolean.TRUE);
                }
                map.put(menu.getId(), menuTree);
            }
        } else {
            for (SysMenu menu : menus) {
                MenuTree menuTree = new MenuTree();
                BeanUtils.copyProperties(menu, menuTree);
                menuTree.setStatus(Boolean.TRUE);
                map.put(menu.getId(), menuTree);
            }
        }

        // 生成权限树
        return getMenuTrees(menus, map);
    }

    private List<MenuTree> getMenuTrees(List<SysMenu> menus, Map<Integer, MenuTree> map) {
        List<MenuTree> root = new ArrayList<>();
        for (SysMenu menu : menus) {
            // 判断当前记录是否有父节点
            if (menu.getPid() == null) {
                root.add(map.get(menu.getId()));
            } else {
                MenuTree menuTree = map.get(menu.getPid());
                menuTree.getChildren().add(map.get(menu.getId()));
            }
        }
        LOGGER.info("权限树：{}", root);
        return root;
    }
}
