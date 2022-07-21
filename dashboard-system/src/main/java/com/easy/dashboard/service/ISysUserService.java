package com.easy.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dashboard.domain.SysUser;
import org.springframework.stereotype.Service;

public interface ISysUserService extends IService<SysUser> {
    SysUser findUserByUserName(String userName);
}
