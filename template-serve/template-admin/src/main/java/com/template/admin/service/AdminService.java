package com.template.admin.service;

import com.template.admin.dto.AdminCreate;
import com.template.admin.dto.AdminLogin;
import com.template.admin.dto.AdminUpdate;
import com.template.admin.bo.SysAdminDetail;
import com.template.common.api.CommonPage;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface AdminService {
    String login(AdminLogin userLogin);

    int logout();

    int updatePassword(String oldPassword, String password);

    int create(AdminCreate userCreate);

    int delete(List<Integer> ids);

    int update(Integer id, AdminUpdate userUpdate);

    CommonPage<?> list(Integer pageNum, Integer pageSize, String keyword);

}
