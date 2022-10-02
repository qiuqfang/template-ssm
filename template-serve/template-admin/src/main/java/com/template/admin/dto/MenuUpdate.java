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
public class MenuUpdate {
    @NotNull(message = "缺少ID")
    private Integer id;
    private Integer pid;
    @NotBlank(message = "请输入菜单标题")
    private String title;
    @NotBlank(message = "请输入菜单路径")
    private String path;
    @NotNull(message = "请确认是否隐藏菜单")
    private Boolean hidden;
}
