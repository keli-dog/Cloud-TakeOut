package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("admin/setmeal")
@RestController
@Slf4j
@Api(tags="套餐管理接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加套餐")
    @CacheEvict(cacheNames = "setmeal", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐：{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     *
     * @param ids
     * @return
     */

    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除套餐：{}", ids);
        setmealService.delete(ids);
        return Result.success();
    }

    /**
     *
     * @param setmealDTO
     * @return
     */

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 启售停售套餐
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启售停售套餐")
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启售停售套餐：status={},id={}", status, id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }

    /**
     *
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    @ApiOperation("根据套餐id获取套餐")
    public Result<SetmealVO> get(@PathVariable Long id) {
        log.info("根据套餐id获取套餐：{}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     *
     * @param setmealPageQueryDTO
     * @return
     */

    @GetMapping("/page")
    @ApiOperation("获取套餐列表")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("获取套餐列表：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

}
