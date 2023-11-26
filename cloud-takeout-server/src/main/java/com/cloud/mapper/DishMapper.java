package com.cloud.mapper;

import com.github.pagehelper.Page;
import com.cloud.annotation.AutoFill;
import com.cloud.dto.DishPageQueryDTO;
import com.cloud.entity.Dish;
import com.cloud.enumeration.OperationType;
import com.cloud.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);


    /**
     * @param ids
     */

    void deleteBatch(List<Long> ids);

    /**
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     *
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id = #{categoryId}&& status = 1")
    List<DishVO> getByCategoryId(Long categoryId);

    /**
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     *
     * @return
     */

    @Select("select * from dish")
    List<Dish> list();
}
