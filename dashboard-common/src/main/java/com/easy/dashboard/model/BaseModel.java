package com.easy.dashboard.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BaseModel {

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonIgnore
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    @JsonIgnore
    private Date updateTime;

    @TableField(value = "create_by",fill = FieldFill.INSERT)
    @JsonIgnore
    private String createdBy;

    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    @JsonIgnore
    private String updatedBy;

    @TableLogic
    @TableField(value = "is_delete")
    @JsonIgnore
    private Boolean isDeleted;
}
