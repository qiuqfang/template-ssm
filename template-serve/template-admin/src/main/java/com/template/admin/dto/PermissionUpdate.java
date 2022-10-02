package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionUpdate {
    @NotNull(message = "缺少ID")
    private Integer id;
    private Integer pid;
    @NotBlank(message = "请输入权限名称")
    private String name;
}
