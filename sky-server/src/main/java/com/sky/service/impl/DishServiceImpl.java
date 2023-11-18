package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.DishDisableFailedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper flavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * @param dishDTO
     */

    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();

        // 拷贝属性
        BeanUtils.copyProperties(dishDTO, dish);

        //插入菜品表
        dishMapper.insert(dish);
        //获取菜品id
        Long id = dish.getId();

        //插入口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!CollectionUtils.isEmpty(flavors) && flavors.size() > 0) {
            //循环插入口味表
            flavors.forEach(flavor -> {
                //在插入口味前，赋值口味对应的菜品id
                flavor.setDishId(id);
                //插入口味表
                flavorMapper.insert(flavor);
            });
        }

    }

    /**
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否启售中
        ids.forEach(it -> {
            Dish dish = dishMapper.getById(it);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //菜品未停售不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
        //判断是否关联套餐
        ids.forEach(it -> {
            Long setmealIdByDishId = setmealDishMapper.getSetmealIdByDishId(it);
            if (setmealIdByDishId != null) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        });
        //删除菜品
        dishMapper.deleteBatch(ids);
        //删除口味
        flavorMapper.deleteBatch(ids);
    }

    /**
     *
     * @param dishDTO
     */
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();

        // 拷贝属性
        BeanUtils.copyProperties(dishDTO, dish);

        //插入菜品表
        dishMapper.update(dish);
        //删除口味表
        List<Long> id =new ArrayList<>();
        id.add(dishDTO.getId());
        flavorMapper.deleteBatch(id);
        //插入口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!CollectionUtils.isEmpty(flavors) && flavors.size() > 0) {
            //循环插入口味表
            flavors.forEach(flavor -> {
                //在插入口味前，赋值口味对应的菜品id
                flavor.setDishId(dish.getId());
                //插入口味表
                flavorMapper.insert(flavor);
            });
        }
    }

    /**
     *
     * @param status
     * @param id
     */

    @Override
    public void startOrStop(Integer status, Long id) {
        if(status == StatusConstant.DISABLE){//在停售时需要判断，菜品是否关联启售套餐
            Long setmealIdByDishId = setmealDishMapper.getSetmealIdByDishId(id);
            if (setmealIdByDishId != null) {
                Setmeal setmeal = setmealMapper.getById(setmealIdByDishId);
                if (setmeal.getStatus() == StatusConstant.ENABLE) {
                    throw new DishDisableFailedException(MessageConstant.DISH_DISABLE_FAILED);
                }
            }
        }
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        dishMapper.update(dish);
    }


    /**
     * @param id
     * @return
     */

    @Transactional
    public DishVO getById(Long id) {
        //根据id查询菜品信息
        Dish dish = dishMapper.getById(id);
        //根据id查询口味信息
        List<DishFlavor> flavors = flavorMapper.getByDishId(id);
        //  创建菜品VO对象
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public List<DishVO> getByCategoryId(Long categoryid) {
        List<DishVO> dishVOList = dishMapper.getByCategoryId(categoryid);
        return dishVOList;
    }

    /**
     * @param dishPageQueryDTO
     * @return
     */


    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<DishVO> listWithFlavor(Long categoryid) {
        List<DishVO> dishVOList = dishMapper.getByCategoryId(categoryid);

        for (DishVO dishVO : dishVOList) {
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = flavorMapper.getByDishId(dishVO.getId());
            dishVO.setFlavors(flavors);
        }

        return dishVOList;
    }
}
