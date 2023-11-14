package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void save(SetmealDTO setmealDTO);

    void delete(List<Long> ids);
    void update(SetmealDTO setmealDTO);
    void startOrStop(Integer status, Long id);
    SetmealVO getById(Long id);
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
