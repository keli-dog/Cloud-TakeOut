package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "搜索相关接口")
@Slf4j
public class SearchContraller {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/category/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> categoryList(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/dish/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> dishList(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        String key = "dish_" + categoryId;
        // 从redis中获取缓存数据
        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (dishVOList != null) {
            log.info("从redis中获取缓存数据成功");
            return Result.success(dishVOList);
        }
        // 从数据库中获取数据
        List<DishVO> dishVOlist = dishService.listWithFlavor(categoryId);
        // 将数据放入redis中缓存
        redisTemplate.opsForValue().set(key, dishVOlist);
        log.info("将数据缓存到redis成功");
        return Result.success(dishVOlist);
    }

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId
     * @return
     */
    @GetMapping("setmeal/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> setmealList(Long categoryId) {
        ;
        log.info("根据分类id查询套餐：{}", categoryId);
        List<Setmeal> list = setmealService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("setmeal/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> setmealDishList(@PathVariable("id") Long id) {
        log.info("根据套餐id查询包含的菜品列表：{}", id);
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
