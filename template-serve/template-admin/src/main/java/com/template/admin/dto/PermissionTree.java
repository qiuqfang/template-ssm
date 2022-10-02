package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionTree {
    private Integer id;
    private Integer pid;
    private String name;
    /**
     * 用来判断当前登录用户是否拥有该资源
     */
    private Boolean status = false;
    private List<PermissionTree> children = new ArrayList<>();
}
