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
public class AdminUpdate {
    @NotNull(message = "缺少ID")
    private Integer id;
    @NotBlank(message = "请输入昵称")
    private String nickName;
    @NotBlank(message = "请选择角色")
    private String roles;
    @NotNull(message = "缺少status")
    private Boolean status;
}
