package com.template.admin.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface RoleDao {
    /**
     * 更新角色数量
     *
     * @param isCreated 是否为添加
     * @param roleMap   角色用户映射
     * @return
     */
    int updateRoleCount(@Param("isCreated") Boolean isCreated, @Param("roleMap") Map<Integer, Integer> roleMap);
}
