package com.easy.dashboard.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.easy.dashboard.model.AuthUser;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/")
public class SaTokenTestController {
    @PostMapping("doLogin")
    public Object doLogin(@RequestBody AuthUser user) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(user.getUsername()) && "123456".equals(user.getPassword())) {
            StpUtil.logout(10001);
            StpUtil.login(10001);
            return StpUtil.getTokenInfo();
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8099/test/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @RequestMapping("getLoginId")
    public Object getLoginId() {
        return StpUtil.getLoginId();
    }

    @RequestMapping("getLoginIdByToken/{loginId}")
    public Object getLoginIdByToken(@PathVariable("loginId") String loginId) {
        return StpUtil.getLoginIdByToken(loginId);
    }

    @RequestMapping("getTokenName")
    public Object getTokenName() {
        return StpUtil.getTokenName();
    }

    @RequestMapping("getTokenValue")
    public Object getTokenValue() {
        return StpUtil.getTokenValue();
    }

    @RequestMapping("getTokenInfo")
    public Object getTokenInfo() {
        return StpUtil.getTokenInfo();
    }

    @RequestMapping("logout")
    public ResultObject logout() {
        StpUtil.logout();
        return ResultObject.success();
    }

    @RequestMapping("logoutByLoginId")
    public ResultObject resultObject(String platform,Boolean isKickOut) {
        String loginId = (String) this.getLoginId();
        if (!isKickOut) {
            if (StringUtil.isNotBlank(platform))
                StpUtil.logout(loginId, platform);
            else
                StpUtil.logout(loginId);
        } else {
            StpUtil.kickout(loginId);                    // 将指定账号踢下线
        }
        return ResultObject.success();
    }


    @RequestMapping("disableAccount")
    public ResultObject disableAccount(Long disableTime) {
        String loginId = (String) this.getLoginId();

        StpUtil.disable(loginId,disableTime);
        return ResultObject.success();
    }

    @RequestMapping("getLogIdIsDisable")
    public ResultObject getLogIdIsDisable() {
        String loginId = (String) this.getLoginId();
        boolean isDisabled =  StpUtil.isDisable(loginId);
        return ResultObject.success(isDisabled);
    }

    @RequestMapping("getDisabledTime")
    public ResultObject getDisabledTime() {
        String loginId = (String) this.getLoginId();
        long disableTime = StpUtil.getDisableTime(loginId);
        return ResultObject.success(disableTime);
    }

    @RequestMapping("untieDisable")
    public ResultObject untieDisable() {
        String loginId = (String) this.getLoginId();
        StpUtil.untieDisable(loginId);
        return ResultObject.success();
    }

    @RequestMapping("generateKeyPair")
    public ResultObject generateKeyPair() throws Exception {
        return ResultObject.success(SaSecureUtil.rsaGenerateKeyPair());
    }
}
