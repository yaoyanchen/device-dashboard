package com.easy.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dashboard.domain.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {
    List<Menu> getMenuItems(Integer userId);
}
