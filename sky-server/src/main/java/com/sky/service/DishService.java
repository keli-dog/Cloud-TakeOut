package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);
    void deleteBatch(List<Long> ids);
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
