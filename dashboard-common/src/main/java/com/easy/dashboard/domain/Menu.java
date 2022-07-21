package com.easy.dashboard.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.easy.dashboard.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Menu extends BaseModel {
    @TableField("menu_id")
    private Integer menuId;

    @TableField("pid")
    @JsonIgnore
    private Integer pId;

    @TableField("sub_count")
    private Integer subCount;

    @TableField("type")
    private Integer type;

    @JsonIgnore
    private String title;

    private String name;

    private String component;

    @TableField("menu_sort")
    @JsonIgnore
    private Integer menuSort;

    @JsonIgnore
    private String icon;

    private String path;

    @TableField("i_frame")
    private String iFrame;

    @TableField("cache")
    @JsonIgnore
    private Boolean cache;

    @TableField("hidden")
    private Boolean hidden;

    @JsonIgnore
    private String permission;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    @TableField(exist = false)
    private Meta meta;
}
