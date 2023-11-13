package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
