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
public class AdminLogin {
    @NotBlank(message = "请输入用户名/账号")
    private String username;
    @NotBlank(message = "请输入密码")
    private String password;
    @NotBlank(message = "请输入验证码")
    private String code;
}
