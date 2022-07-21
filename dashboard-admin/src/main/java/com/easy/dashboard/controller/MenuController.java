package com.easy.dashboard.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final IMenuService menuService;
    @GetMapping("auth/list")
    @SaCheckLogin
    public ResultObject authList() {
        Integer userId = Integer.parseInt ((String) StpUtil.getLoginId());
        return ResultObject.success(menuService.getMenuItems(userId));
    }
}
