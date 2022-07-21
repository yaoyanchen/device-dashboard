package com.easy.dashboard.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.dashboard.domain.Menu;
import com.easy.dashboard.mapper.MenuMapper;
import com.easy.dashboard.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    private final MenuMapper menuMapper;


    @Override
    public List<Menu> getMenuItems(Integer userId) {
        final List<Integer> roles = menuMapper.getRoles(userId);
        return menuMapper.getMenus(roles);
    }
}
