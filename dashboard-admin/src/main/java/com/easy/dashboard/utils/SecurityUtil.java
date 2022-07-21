package com.easy.dashboard.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.easy.dashboard.domain.SysUser;
import com.easy.dashboard.interceptor.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil extends StpUtil {
    private final RedisUtil redisUtil;

    public SysUser getCurrentUser() {
        Object sysUser = redisUtil.get(getTokenValue());
        if (ObjectUtil.isNull(sysUser)) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED,"当前登录状态过期");
        }
        if (sysUser instanceof SysUser) {
            return (SysUser) sysUser;
        }
        throw new BadRequestException(HttpStatus.UNAUTHORIZED, "找不到当前登录的信息");
    }

    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
}
