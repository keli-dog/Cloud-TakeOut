package com.cloud.controller.user;

import com.cloud.context.BaseContext;
import com.cloud.dto.ShoppingCartDTO;
import com.cloud.entity.ShoppingCart;
import com.cloud.result.Result;
import com.cloud.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车,信息:"+shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }
    @PostMapping("/sub")
    @ApiOperation("减少购物车")
    public Result update(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("减少购物车,信息:"+shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public Result<List<ShoppingCart>> list(){
        log.info("获取购物车列表");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        log.info("删除购物车,id:"+ BaseContext.getCurrentId());
        shoppingCartService.clean();
        return Result.success();
    }
}
