package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入数据
     *
     * @param flavor
     */
    @Insert("insert into dish_flavor(dish_id,name,value) VALUES" + " (#{dishId},#{name},#{value}) ")
    void insert(DishFlavor flavor);

    /**
     *
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
