package com.template.admin.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysAdminDetail implements Serializable {
    private Integer id;
    private String username;
    private String nickName;
    private String avatar;
    private List<String> roles = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();
    private List<String> menus = new ArrayList<>();
}
