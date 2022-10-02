package com.template.admin.service.impl;

import com.template.admin.bo.SysAdminDetail;
import com.template.admin.dto.RoleCreate;
import com.template.admin.dto.RoleUpdate;
import com.template.admin.service.RoleService;
import com.template.common.api.CommonPage;
import com.template.security.exception.ApiException;
import com.template.mbg.mapper.SysRoleMapper;
import com.template.mbg.model.SysRole;
import com.template.mbg.model.SysRoleExample;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    private SysRoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(SysRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public int create(RoleCreate roleCreate) {
        int count;
        String name = roleCreate.getName();
        SysRoleExample roleExample = new SysRoleExample();
        roleExample.createCriteria().andNameEqualTo(name);
        List<SysRole> roles = roleMapper.selectByExample(roleExample);
        if (!CollectionUtils.isEmpty(roles)) {
            throw new ApiException("角色已存在");
        }
        SysRole role = new SysRole();
        role.setCreateTime(new Date());
        role.setCount(0);
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        role.setAdminId(adminDetail.getId());
        BeanUtils.copyProperties(roleCreate, role);
        count = roleMapper.insertSelective(role);
        return count;
    }

    @Override
    public int delete(List<Integer> ids) {
        int count = 0;
        if (ids.contains(1)) {
            throw new ApiException("超级管理员角色为最高权限角色无法删除。");
        }
        // 判断需要删除的角色是否被使用
        SysRoleExample roleExample = new SysRoleExample();
        roleExample.createCriteria().andIdIn(ids);
        List<SysRole> roles = roleMapper.selectByExample(roleExample);
        for (SysRole role : roles) {
            if (role.getCount() != 0) {
                throw new ApiException("角色被使用，无法删除");
            }
        }
        roleExample.createCriteria().andCountEqualTo(0);
        count = roleMapper.deleteByExample(roleExample);
        return count;
    }

    @Override
    public int update(Integer id, RoleUpdate roleUpdate) {
        int count;
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if (role == null) {
            throw new ApiException("请确认角色是否存在");
        }
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        if (!role.getAdminId().equals(adminDetail.getId())) {
            throw new ApiException("该角色不是您创建，请联系超级管理员");
        }
        BeanUtils.copyProperties(roleUpdate, role);
        role.setUpdateTime(new Date());
        count = roleMapper.updateByPrimaryKeySelective(role);
        return count;
    }

    @Override
    public CommonPage<?> list(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        SysRoleExample roleExample = new SysRoleExample();
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        /* 获取当前登录账号添加的角色（分页） */
        roleExample.createCriteria().andAdminIdEqualTo(adminDetail.getId()).andNameLike("%" + keyword + "%");
        List<SysRole> roles = roleMapper.selectByExample(roleExample);
        return CommonPage.restPage(roles);
    }

    @Override
    public List<SysRole> getRoleByAdminId() {
        SysRoleExample roleExample = new SysRoleExample();
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        /* 获取当前登录账号添加的角色 */
        roleExample.createCriteria().andAdminIdEqualTo(adminDetail.getId());
        List<SysRole> sysRoles = roleMapper.selectByExample(roleExample);
        return sysRoles;
    }
}
