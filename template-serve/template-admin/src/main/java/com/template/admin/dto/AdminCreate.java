package com.template.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminCreate {
    @NotBlank(message = "请输入用户名")
    private String username;
    @Pattern(message = "请输入8位数以上密码且密码包含大小写字母、数字、符号", regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[`~!@#%&_=;':\"<>,/\\$\\^\\*\\(\\)\\+\\{\\}\\|\\?\\[\\]\\\\\\.\\-])[0-9A-Za-z`~!@#%&_=;':\"<>,/\\$\\^\\*\\(\\)\\+\\{\\}\\|\\?\\[\\]\\\\\\.\\-]{8,}$")
    private String password;
    @NotBlank(message = "请输入昵称")
    private String nickName;
    private String avatar;
    @NotBlank(message = "请选择角色")
    private String roles;
}
