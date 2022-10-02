package com.template.admin.service;

import com.template.admin.dto.RoleCreate;
import com.template.admin.dto.RoleUpdate;
import com.template.common.api.CommonPage;
import com.template.mbg.model.SysRole;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface RoleService {

    int create(RoleCreate roleCreate);

    int delete(List<Integer> ids);

    int update(Integer id, RoleUpdate roleUpdate);

    CommonPage<?> list(Integer pageNum, Integer pageSize, String keyword);

    List<SysRole> getRoleByAdminId();
}
