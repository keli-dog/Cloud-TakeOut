package com.cloud.controller.admin;

import com.cloud.dto.DishDTO;
import com.cloud.dto.DishPageQueryDTO;
import com.cloud.result.PageResult;
import com.cloud.result.Result;
import com.cloud.service.DishService;
import com.cloud.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("admin/dish")
@Api(tags="菜品管理接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.save(dishDTO);
        // 清除缓存
        clearCache("dish_"+dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteById(@RequestParam List<Long> ids) {
        log.info("删除菜品：{}", ids);
        dishService.deleteBatch(ids);
        // 清除缓存
        clearCache("dish_*");
        return Result.success();
    }

    /**
     *
     * @param dishDTO
     * @return
     */

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.update(dishDTO);
        // 清除缓存
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 启售停售菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启售停售菜品")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启售停售菜品：status={},id={}", status, id);
        dishService.startOrStop(status,id);
        // 清除缓存
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据菜品id获取菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据菜品id获取菜品：{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取菜品列表")
    public Result<List<DishVO>> list(@RequestParam Long categoryId) {
        log.info("获取菜品列表：{}", categoryId);
        List<DishVO> dishVOlist = dishService.getByCategoryId(categoryId);
        return Result.success(dishVOlist);
    }

    /**
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("获取菜品分页详情")
    public Result<PageResult> detail(DishPageQueryDTO dishPageQueryDTO) {
        log.info("获取菜品详情：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    
    /**
     * 清理缓存redis数据
     * @param pattern
     */
    private  void clearCache(String pattern){
        redisTemplate.keys(pattern).forEach(key->{
            redisTemplate.delete(key);
        });
    }
}
