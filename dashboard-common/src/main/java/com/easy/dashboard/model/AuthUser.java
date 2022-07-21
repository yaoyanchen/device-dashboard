package com.easy.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthUser {
    private String username;
    private String password;
    private String uuid;
    private String code;
}
