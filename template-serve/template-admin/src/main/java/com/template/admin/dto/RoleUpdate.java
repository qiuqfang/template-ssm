package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdate {
    @NotNull(message = "缺少ID")
    private Integer id;
    @NotBlank(message = "请输入角色名")
    private String name;
    @NotBlank(message = "请选择菜单")
    private String menus;
    @NotBlank(message = "请选择权限")
    private String permissions;
}
