package com.cloud.controller.admin;

import com.cloud.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    public static final String KEY= "shop:status";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "分类列表", notes = "分类列表")
    public Result setStatus(@PathVariable Integer status) {
        log.info("更改状态为：{}", status == 1 ? "营业中" : "打烊了");
        redisTemplate.opsForValue().set("shop:status", status);
        return Result.success();
    }

    /**
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("店铺状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态：{}", status==1?"营业中": "打烊了");
        return Result.success(status);
    }
}
