package com.cloud.mapper;

import com.github.pagehelper.Page;
import com.cloud.annotation.AutoFill;
import com.cloud.dto.SetmealPageQueryDTO;
import com.cloud.entity.Setmeal;
import com.cloud.enumeration.OperationType;
import com.cloud.vo.DishItemVO;
import com.cloud.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * @param ids
     */

    void deleteBatch(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> getByCategoryId(Long categoryId);

    /**
     * @param id
     * @return
     */
    @Select("select sd.name,sd.copies,d.image,d.description from setmeal_dish sd left join dish d " +
            "on sd.dish_id=d.id" +
            " where setmeal_id = #{id}")
    List<DishItemVO> getDishItemBySetmealId(Long id);

    /**
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     *
     * @return
     */
    @Select("select * from setmeal")
    List<Setmeal> list();

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);


}

