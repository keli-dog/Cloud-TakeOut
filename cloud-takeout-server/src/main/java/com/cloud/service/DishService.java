package com.cloud.service;

import com.cloud.dto.DishDTO;
import com.cloud.dto.DishPageQueryDTO;
import com.cloud.result.PageResult;
import com.cloud.vo.DishVO;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);
    void deleteBatch(List<Long> ids);
    void update(DishDTO dishDTO);
    void startOrStop(Integer status, Long id);
    DishVO getById(Long id);
    List<DishVO> getByCategoryId(Long categoryid);
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    List<DishVO> listWithFlavor(Long categoryId);
}
