package com.template.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.template.admin.dao.RoleDao;
import com.template.admin.dto.AdminCreate;
import com.template.admin.dto.AdminLogin;
import com.template.admin.dto.AdminUpdate;
import com.template.admin.service.AdminService;
import com.template.common.api.CommonPage;
import com.template.common.service.RedisService;
import com.template.security.exception.ApiException;
import com.template.security.util.EncryptionUtil;
import com.template.security.util.JwtTokenUtil;
import com.template.mbg.mapper.SysAdminMapper;
import com.template.mbg.model.SysAdminExample;
import com.template.mbg.model.SysAdmin;
import com.template.admin.bo.SysAdminDetail;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
    private static final Integer SUPPER_ADMIN_ID = 1;

    @Value("${security.tokenHeader}")
    private String tokenHeader;
    @Value("${security.tokenHead}")
    private String tokenHead;

    private EncryptionUtil encryptionUtil;
    private JwtTokenUtil jwtTokenUtil;
    private SysAdminMapper adminMapper;
    private RoleDao roleDao;
    private RedisService redisService;

    @Autowired
    public AdminServiceImpl(EncryptionUtil encryptionUtil, JwtTokenUtil jwtTokenUtil, SysAdminMapper adminMapper, RoleDao roleDao, RedisService redisService) {
        this.encryptionUtil = encryptionUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.adminMapper = adminMapper;
        this.roleDao = roleDao;
        this.redisService = redisService;
    }

    @Override
    public String login(AdminLogin adminLogin) {
        String username = adminLogin.getUsername();
        String password = adminLogin.getPassword();
        String code = (String) redisService.get("code");
        if (code == null) {
            throw new ApiException("??????????????????");
        }
        if (!code.equals(adminLogin.getCode())) {
            throw new ApiException("??????????????????????????????");
        } else {
            redisService.del("code");
        }
        // ???????????????\?????????????????????????????????
        SysAdminExample adminExample = new SysAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username);
        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);
        if (CollectionUtil.isEmpty(admins)) {
            throw new ApiException("??????????????????!");
        }
        boolean verify = false;
        for (SysAdmin admin : admins) {
            verify = encryptionUtil.verifyPassword(admin.getCreateTime(), admin.getPassword(), password);
            if (verify) {
                if (!admin.getStatus()) {
                    throw new ApiException("??????????????????");
                }
                String token = jwtTokenUtil.generateToken(username);

                // ?????????token???????????????????????????????????????
                admin.setLoginTime(new Date());
                admin.setToken(token);
                adminMapper.updateByPrimaryKeySelective(admin);
                return token;
            }
        }
        throw new ApiException("????????????");
    }

    @Override
    public int logout() {
        Subject subject = SecurityUtils.getSubject();
        SysAdminDetail adminDetail = (SysAdminDetail) subject.getPrincipal();
        SysAdmin admin = adminMapper.selectByPrimaryKey(adminDetail.getId());
        admin.setToken(null);
        int count = adminMapper.updateByPrimaryKey(admin);
        return count;
    }

    @Override
    public int updatePassword(String oldPassword, String password) {
        int count = 0;
        // ??????token????????????????????????????????????/??????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(tokenHeader).replace(tokenHead, "");
        String username = jwtTokenUtil.getUserNameFromToken(token);
        // ???????????????/?????????????????????????????????
        SysAdminExample adminExample = new SysAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username);
        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);
        if (CollectionUtils.isEmpty(admins)) {
            return count;
        }
        SysAdmin admin = admins.get(0);
        /* ???????????????????????????????????????????????????????????????????????? */
        if (!encryptionUtil.verifyPassword(admin.getCreateTime(), admin.getPassword(), oldPassword)) {
            return count;
        }
        admin.setPassword(encryptionUtil.encodePassword(admin.getCreateTime(), password));
        admin.setUpdateTime(new Date());
        count = adminMapper.updateByPrimaryKeySelective(admin);
        return count;
    }

    @Override
    public int create(AdminCreate adminCreate) {
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        int count;
        String username = adminCreate.getUsername();
        SysAdminExample adminExample = new SysAdminExample();
        adminExample.createCriteria().andPidEqualTo(adminDetail.getId()).andUsernameEqualTo(username);
        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);
        if (!CollectionUtils.isEmpty(admins)) {
            throw new ApiException("???????????????");
        }
        SysAdmin admin = new SysAdmin();
        // ??????????????????
        admin.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80");
        admin.setCreateTime(new Date());
        admin.setUpdateTime(new Date());
        /* ???????????? */
        admin.setStatus(Boolean.TRUE);
        /* ??????????????????????????????ID */
        admin.setPid(adminDetail.getId());

        BeanUtils.copyProperties(adminCreate, admin);
        admin.setPassword(encryptionUtil.encodePassword(admin.getCreateTime(), admin.getPassword()));
        count = adminMapper.insertSelective(admin);

        // ??????????????????
        admins.add(admin);
        updateRoleCount(Boolean.TRUE, admins);

        return count;
    }

    @Override
    public int delete(List<Integer> ids) {
        int count = 0;
        if (ids.contains(1)) {
            throw new ApiException("???????????????????????????");
        }
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        SysAdminExample adminExample = new SysAdminExample();
        // ???????????????????????????????????????????????????
        adminExample.createCriteria().andPidEqualTo(adminDetail.getId()).andIdIn(ids);
        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);
        count = adminMapper.deleteByExample(adminExample);
        if (count == 0) {
            throw new ApiException("???????????????????????????");
        }

        updateRoleCount(Boolean.FALSE, admins);

        return count;
    }

    @Override
    public int update(Integer id, AdminUpdate adminUpdate) {
        int count;
        SysAdmin admin = adminMapper.selectByPrimaryKey(id);
        if (admin == null) {
            throw new ApiException("???????????????????????????");
        }
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        if (!admin.getPid().equals(adminDetail.getId())) {
            throw new ApiException("???????????????????????????????????????????????????");
        }
        List<SysAdmin> admins = new ArrayList<>();
        admins.add(admin);
        // ???????????????????????????
        updateRoleCount(Boolean.FALSE, admins);
        BeanUtils.copyProperties(adminUpdate, admin);
        admin.setUpdateTime(new Date());
        count = adminMapper.updateByPrimaryKeySelective(admin);
        // ???????????????????????????
        updateRoleCount(Boolean.TRUE, admins);
        return count;
    }

    @Override
    public CommonPage<?> list(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        SysAdminExample adminExample = new SysAdminExample();
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();

        /* select * from sys_admin WHERE ( pid = ? and username like ? ) or( pid = ? and nick_name like ? ) LIMIT ?  */
        adminExample.createCriteria().andPidEqualTo(adminDetail.getId()).andUsernameLike("%" + keyword + "%");
        adminExample.or(adminExample.createCriteria().andPidEqualTo(adminDetail.getId()).andNickNameLike("%" + keyword + "%"));

        List<SysAdmin> admins = adminMapper.selectByExample(adminExample);
        return CommonPage.restPage(admins);
    }

    private void updateRoleCount(boolean isCreated, List<SysAdmin> admins) {
        Map<Integer, Integer> roleMap = new HashMap<>();
        Set<Integer> integerSet = roleMap.keySet();
        for (SysAdmin admin : admins) {
            if ("".equals(admin.getRoles())) {
                return;
            }
            String[] roles = admin.getRoles().split(",");
            List<Integer> ids = new ArrayList<>();
            for (String roleId : roles) {
                ids.add(Integer.valueOf(roleId));
            }
            for (Integer id : ids) {
                if (integerSet.contains(id)) {
                    roleMap.put(id, roleMap.get(id) + 1);
                } else {
                    roleMap.put(id, 1);
                }
            }
        }
        roleDao.updateRoleCount(isCreated, roleMap);
    }
}
