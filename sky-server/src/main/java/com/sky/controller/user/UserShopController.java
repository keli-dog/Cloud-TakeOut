package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class UserShopController {
    public static final String KEY= "shop:status";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @return
     */
    @GetMapping
    @ApiOperation("店铺状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态：{}", status==1?"营业中": "打烊了");
        return Result.success(status);
    }
}
