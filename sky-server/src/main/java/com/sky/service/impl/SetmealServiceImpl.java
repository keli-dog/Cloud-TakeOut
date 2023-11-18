package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;


    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //插入套餐表
        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        //插入套餐关联菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(setmealDishes) && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                //设置套餐id
                setmealDish.setSetmealId(id);
                //插入套餐关联菜品信息
                setmealDishMapper.insert(setmealDish);
            });
        }

    }

    @Override
    public void delete(List<Long> ids) {
        //删除套餐
        setmealMapper.deleteBatch(ids);
        //删除套餐关联菜品
        setmealDishMapper.deleteBatch(ids);
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        //拷贝属性
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐表
        setmealMapper.update(setmeal);
        //  删除套餐关联菜品
        List<Long> id = new ArrayList<>();
        id.add(setmeal.getId());
        setmealDishMapper.deleteBatch(id);
        //  增加套餐关联菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(setmealDishes) && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                //设置套餐id
                setmealDish.setSetmealId(setmeal.getId());
                //插入套餐关联菜品信息
                setmealDishMapper.insert(setmealDish);
            });
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if(status== StatusConstant.ENABLE){//在启售套餐时需要判断套餐所含菜品是否存在停售情况
            List<SetmealDish> setmealDishList = setmealDishMapper.getBySetmealId(id);
            setmealDishList.forEach(setmealDish -> {
                Dish dish = dishMapper.getById(setmealDish.getDishId());
                if(dish.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        setmealMapper.update(setmeal);
    }


    @Override
    public SetmealVO getById(Long id) {
        //  查询套餐
        Setmeal setmeal = setmealMapper.getById(id);
        //  查询套餐菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        //  构建VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public List<Setmeal> getByCategoryId(Long categoryId) {
        List<Setmeal> setmealList = setmealMapper.getByCategoryId(categoryId);
        return setmealList;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishItemVOList = setmealMapper.getDishItemBySetmealId(id);
        log.warn("dishItemVOList:{}", dishItemVOList);
        return dishItemVOList;
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
