package com.easy.dashboard.model;

import com.easy.dashboard.domain.SysUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AuthUserDto {
    private SysUser user;
    private List<String> roles;
    private List<String> dataScopes;
}
