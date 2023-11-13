package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * @param dishId
     * @return
     */
    @Select("select * from setmeal_dish where id = #{dishId}")
    Long getSetmealIdByDishId(Long dishId);
}
