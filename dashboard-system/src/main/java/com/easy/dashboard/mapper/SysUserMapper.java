package com.easy.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.dashboard.domain.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}
