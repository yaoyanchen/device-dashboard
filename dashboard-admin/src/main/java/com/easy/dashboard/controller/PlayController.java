package com.easy.dashboard.controller;

import com.easy.dashboard.domain.GenshinImpact;
import com.easy.dashboard.model.ResultObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/play")
public class PlayController {
    @PostMapping("/lotto")
    public ResultObject lotto(@RequestParam Map<String,Object> param) {
        int count = Integer.parseInt(param.get("count").toString());
        int type = Integer.parseInt(param.get("type").toString());
        GenshinImpact.chou = count;
        GenshinImpact.type = type;
        List<String> list = GenshinImpact.Lottery();
        StringBuilder menu = GenshinImpact.menu;
        Map<String,Object> result = new HashMap<>();
        result.put("result", list);
        result.put("menu", menu);
        return ResultObject.success(result);
    }
}
