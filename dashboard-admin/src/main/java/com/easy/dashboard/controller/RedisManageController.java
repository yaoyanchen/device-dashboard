package com.easy.dashboard.controller;

import com.easy.dashboard.interceptor.BadRequestException;
import com.easy.dashboard.model.Page;
import com.easy.dashboard.model.RedisModel;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.utils.ClassUtil;
import com.easy.dashboard.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/redis/manage")
@RequiredArgsConstructor
public class RedisManageController extends BaseController
{
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/getAllKeys/{dataBaseIndex}")
    public ResultObject getAllKeys(@PathVariable Integer dataBaseIndex, Page page) {
        switchDataBase(dataBaseIndex);
        Set<String> keys = redisTemplate.keys("*");
        return getResultObject(page, keys);
    }

    @GetMapping("/getValueByKey/{dataBaseIndex}/{key}")
    public ResultObject getValueByKey(@PathVariable Integer dataBaseIndex,@PathVariable String key) {
        switchDataBase(dataBaseIndex);
        Object o = redisTemplate.opsForValue().get(key);
        if (o != null) {
            Map<String, Object> result = new HashMap<>();
            //缓存的信息
            result.put("info", o);
            result.put("type", ClassUtil.getClassTypeByObject(o));
            result.put("time", redisTemplate.getExpire(key));
            return ResultObject.success(result);
        }
        else {
            return ResultObject.success();
        }
    }

    @PostMapping("/deleteRedisByKey/{dataBaseIndex}/{key}")
    public ResultObject deleteRedisByKey(@PathVariable Integer dataBaseIndex,@PathVariable String key) {
        switchDataBase(dataBaseIndex);
        Boolean delete = redisTemplate.delete(key);
        return result(delete);
    }

    @GetMapping("/searchKeysRedisByKey/{dataBaseIndex}/{key}")
    public ResultObject searchKeysRedisByKey(@PathVariable Integer dataBaseIndex,@PathVariable String key,Page page) {
        switchDataBase(dataBaseIndex);
        Set<String> keys = redisTemplate.keys("*" + key + "*");
        return getResultObject(page, keys);
    }

    private ResultObject getResultObject(Page page, Set<String> keys) {
        if (keys != null && keys.size() > 0) {
            List<String> list = new ArrayList<>(keys);
            List<RedisModel> pageList = new ArrayList<>();
            list.forEach(x -> {
                Object o = redisTemplate.opsForValue().get(x);
                RedisModel model = new RedisModel();
                model.setRedisKey(x);
                model.setRedisType(ClassUtil.getClassTypeByObject(o));
                model.setExpireTime(redisTemplate.getExpire(x));
                model.setValue(o);
                pageList.add(model);
            });
            return ResultObject.success(PageUtil.page(pageList, page));
        }
        else {
            return ResultObject.success();
        }
    }

    @PostMapping("/updateRedisInfo/{dataBaseIndex}/{key}")
    public ResultObject updateRedisInfo(@PathVariable Integer dataBaseIndex, @PathVariable String key, @RequestBody RedisModel redisModel) {
        switchDataBase(dataBaseIndex);
        redisTemplate.opsForValue().set(key, redisModel.getValue());
        return ResultObject.success();
    }

    private void switchDataBase(Integer index) {
        if (index == null) {
            return;
        }
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        if (lettuceConnectionFactory != null) {
            if (lettuceConnectionFactory.getDatabase() == index) {
                return;
            }
            lettuceConnectionFactory.setDatabase(index);
            lettuceConnectionFactory.setShareNativeConnection(false);
            redisTemplate.setConnectionFactory(lettuceConnectionFactory);
            lettuceConnectionFactory.resetConnection();
            lettuceConnectionFactory.afterPropertiesSet();
        }
        else {
            throw new BadRequestException("Redis初始化失败!");
        }
    }
}
