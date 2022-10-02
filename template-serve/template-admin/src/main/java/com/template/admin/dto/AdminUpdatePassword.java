package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminUpdatePassword {
    @NotNull(message = "请输入原密码")
    private String oldPassword;
    @NotNull(message = "请输入新密码")
    private String password;
}
