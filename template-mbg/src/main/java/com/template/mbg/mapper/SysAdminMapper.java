package com.template.mbg.mapper;

import com.template.mbg.model.SysAdmin;
import com.template.mbg.model.SysAdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAdminMapper {
    long countByExample(SysAdminExample example);

    int deleteByExample(SysAdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysAdmin record);

    int insertSelective(SysAdmin record);

    List<SysAdmin> selectByExample(SysAdminExample example);

    SysAdmin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysAdmin record, @Param("example") SysAdminExample example);

    int updateByExample(@Param("record") SysAdmin record, @Param("example") SysAdminExample example);

    int updateByPrimaryKeySelective(SysAdmin record);

    int updateByPrimaryKey(SysAdmin record);
}