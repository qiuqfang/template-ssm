package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.reflect.generics.tree.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTree {
    private Integer id;
    private Integer pid;
    private String title;
    private String path;
    private Boolean hidden;
    // 用来判断当前登录用户是否拥有该菜单
    private Boolean status = false;
    private List<MenuTree> children = new ArrayList<>();
}
