package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);
    void deleteBatch(List<Long> ids);
    void update(DishDTO dishDTO);
    void startOrStop(Integer status, Long id);
    DishVO getById(Long id);
    List<DishVO> getByCategoryId(Long categoryid);
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
