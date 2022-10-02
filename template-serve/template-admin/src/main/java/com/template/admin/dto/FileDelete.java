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
@NoArgsConstructor
@AllArgsConstructor
public class FileDelete {
    @NotBlank(message = "缺少文件夹名")
    private String folderName;
    @NotNull(message = "缺少文件名")
    private String name;
}
