package com.easy.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.dashboard.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Integer> getRoles(Integer userId);

    List<Menu> getMenus(List<Integer> roleId);
}
