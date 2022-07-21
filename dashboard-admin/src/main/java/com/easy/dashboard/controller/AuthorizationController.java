package com.easy.dashboard.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.easy.dashboard.config.RsaProperties;
import com.easy.dashboard.config.SaTokenDevice;
import com.easy.dashboard.domain.SendEmail;
import com.easy.dashboard.domain.SysUser;
import com.easy.dashboard.interceptor.BadRequestException;
import com.easy.dashboard.mapper.SendEmailMapper;
import com.easy.dashboard.model.AuthUser;
import com.easy.dashboard.model.EmailDTO;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.service.ISysUserService;
import com.easy.dashboard.utils.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final RedisUtil redisUtil;
    private final ISysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    @GetMapping("/code")
    public ResultObject getCode() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,20);
        String imageBase64 = lineCaptcha.getImageBase64();
        String code = lineCaptcha.getCode();
        String uuid = UUID.fastUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img","data:image/jpg;base64," + imageBase64);
        redisUtil.set(uuid,code,60);
        return ResultObject.success(map);
    }

    @PostMapping("/web/login")
    public ResultObject loginByWeb(@RequestBody AuthUser user) throws Exception {
        return this.login(user,SaTokenDevice.WEB);
    }

    @SaCheckLogin
    @GetMapping("/info")
    public ResultObject info() {
        return ResultObject.success(redisUtil.get(StpUtil.getTokenValue()));
    }

    @SaCheckLogin
    @GetMapping("logout")
    public ResultObject logout() {
        // 删除用户缓存
        redisUtil.del(StpUtil.getTokenValue());
        StpUtil.logout();
        return ResultObject.success();
    }

    @PostMapping("kickOut/disabled/account")
    @SaCheckRole("admin")
    public ResultObject kickOutAndDisable(@RequestBody SysUser param) {
        SysUser user = sysUserService.getById(param.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BadRequestException("数据异常");
        }
        // 踢下线
        StpUtil.kickout(user.getUserId());
        // 封禁
        StpUtil.disable(user.getUserId(),-1);
        user.setEnabled(false);
        sysUserService.updateById(user);
        return ResultObject.success();
    }

    @PostMapping("kickOut/user")
    @SaCheckRole("admin")
    public ResultObject kickOut(@RequestBody SysUser param) {
        SysUser user = sysUserService.getById(param.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BadRequestException("数据异常");
        }
        // 踢下线
        StpUtil.kickout(user.getUserId());
        return ResultObject.success();
    }

    @PostMapping("disable/user")
    @SaCheckRole("admin")
    public ResultObject disableUser(@RequestBody SysUser param,Long time) {
        SysUser user = sysUserService.getById(param.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BadRequestException("数据异常");
        }
        // 封禁时间
        StpUtil.disable(user.getUserId(),time);
        return ResultObject.success();
    }

    @GetMapping("code/email")
    public ResultObject codeEmail(String email) {
        if (StringUtil.isEmpty(email))
        {
            throw new BadRequestException("请输入邮箱地址");
        }
        String code = StringUtil.creatMailCode();
        EmailDTO emailDTO = new EmailDTO();
        String content = "本次请求的验证码为：" + code + ",验证码五分钟内有效。";
        sendEmailMapper.insert(new SendEmail(email,content,code,new Date()));
        emailDTO.setTitle("验证码");
        emailDTO.setContent(content);
        emailDTO.setEmail(email);
        redisUtil.set(Common.getCodeEmailKey(email),code, Common.getSecondByMinute(5));
        emailUtil.sendEmail(emailDTO);
        return ResultObject.success();
    }



    private final SendEmailMapper sendEmailMapper;



    public ResultObject login(AuthUser user,String device) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,user.getPassword());
        if (StringUtil.isEmpty(user.getUuid()) || StringUtil.isEmpty(user.getCode())) {
            throw new BadRequestException("请输入验证码");
        }
        String code = (String) redisUtil.get(user.getUuid());
        redisUtil.del(user.getUuid());
        if(StringUtil.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtil.isBlank(user.getCode()) || !user.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        SysUser sysUser = sysUserService.findUserByUserName(user.getUsername());
        if (ObjectUtil.isNull(sysUser)) {
            throw new BadRequestException("账号或者密码错误");
        } else {
            if (!sysUser.getEnabled()) {
                throw new BadRequestException("账号被禁用");
            }
            boolean match = passwordEncoder.matches(password, sysUser.getPassword());
            if (match) {
                StpUtil.logout(sysUser.getUserId());
                StpUtil.login(sysUser.getUserId(), device);
                Map<String, Object> authUserInfo = new HashMap<>();
                String token = StpUtil.getTokenValue();
                authUserInfo.put("token", token);
                authUserInfo.put("user", sysUser);
                redisUtil.set(StpUtil.getTokenValue(),sysUser);
                return ResultObject.success(authUserInfo);
            } else {
                throw new BadRequestException("账号或者密码错误");
            }
        }
    }

}
