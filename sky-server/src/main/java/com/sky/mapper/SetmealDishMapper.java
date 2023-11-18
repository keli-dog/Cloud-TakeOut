package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     *
     * @param setmealDish
     */
    @Insert("insert into setmeal_dish (setmeal_id,dish_id,name,price,copies) VALUES"+
            " (#{setmealId}, #{dishId}, #{name}, #{price},#{copies})")
    void insert(SetmealDish setmealDish);

    /**
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据套餐id获取套餐菜单
     *
     * @param setmeal_id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmeal_id}")
    List<SetmealDish> getBySetmealId(Long setmeal_id);
    /**
     * @param dishId
     * @return
     */
    @Select("select setmeal_id from setmeal_dish where dish_id = #{dishId}")
    Long getSetmealIdByDishId(Long dishId);
}
