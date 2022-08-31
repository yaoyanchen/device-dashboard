package com.easy.dashboard.utils;

import com.easy.dashboard.model.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageUtil extends cn.hutool.core.util.PageUtil {
    public static <T> Page page(List<T> list,Page page) {
        Page result = new Page(page.getPageIndex(), page.getPageNum());
        result.setList(list.stream()
                .skip((long) (page.getPageIndex() - 1) * page.getPageNum()).limit(page.getPageNum())
                .collect(Collectors.toList()));
        result.setTotal((long) list.size());
        result.setPageIndex(page.getPageIndex());
        result.setPageNum(page.getPageNum());
        return result;
    }
}
