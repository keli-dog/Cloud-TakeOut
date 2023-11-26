package com.cloud.service;

import com.cloud.dto.SetmealDTO;
import com.cloud.dto.SetmealPageQueryDTO;
import com.cloud.entity.Setmeal;
import com.cloud.result.PageResult;
import com.cloud.vo.DishItemVO;
import com.cloud.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void save(SetmealDTO setmealDTO);
    void delete(List<Long> ids);
    void update(SetmealDTO setmealDTO);
    void startOrStop(Integer status, Long id);
    SetmealVO getById(Long id);
    List<Setmeal> getByCategoryId(Long categoryId);
    List<DishItemVO> getDishItemById(Long id);
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
