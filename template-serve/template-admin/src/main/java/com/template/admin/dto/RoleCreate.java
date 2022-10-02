package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreate {
    @NotBlank(message = "请输入角色名")
    private String name;
    @NotBlank(message = "请选择菜单")
    private String menus;
    @NotBlank(message = "请选择权限")
    private String permissions;
}
