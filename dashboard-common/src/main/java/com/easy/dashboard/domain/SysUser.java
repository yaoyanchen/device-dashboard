package com.easy.dashboard.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.easy.dashboard.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends BaseModel {
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;
    /**
    * 部门名称
    */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
    * 用户名
    */
    @TableField(value = "username")
    private String username;

    /**
    * 昵称
    */
    @TableField(value = "nick_name")
    private String nickName;

    /**
    * 性别
    */
    @TableField(value = "gender")
    private String gender;

    /**
    * 手机号码
    */
    @TableField(value = "phone")
    private String phone;

    /**
    * 邮箱
    */
    @TableField(value = "email")
    private String email;

    /**
    * 头像地址
    */
    @TableField(value = "avatar_name")
    private String avatarName;

    /**
    * 头像真实路径
    */
    @TableField(value = "avatar_path")
    private String avatarPath;

    /**
    * 密码
    */
    @TableField(value = "password")
    @JsonIgnore
    private String password;

    /**
    * 是否为admin账号
    */
    @TableField(value = "is_admin")
    @JsonIgnore
    private Boolean isAdmin;

    /**
    * 状态：1启用、0禁用
    */
    @TableField(value = "enabled")
    @JsonIgnore
    private Boolean enabled;

    /**
    * 修改密码的时间
    */
    @TableField(value = "pwd_reset_time")
    private Date pwdResetTime;
}
