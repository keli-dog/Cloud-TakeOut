package com.cloud.service.impl;

import com.cloud.context.BaseContext;
import com.cloud.dto.ShoppingCartDTO;
import com.cloud.entity.Dish;
import com.cloud.entity.Setmeal;
import com.cloud.entity.ShoppingCart;
import com.cloud.mapper.DishMapper;
import com.cloud.mapper.SetmealMapper;
import com.cloud.mapper.ShoppingCartMapper;
import com.cloud.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //创建购物车对象
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //设置用户user_id
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //查询商品是否在购物车中
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && list.size() > 0) {
            //如果在，执行更新操作,数量加1
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //如果不在，执行插入操作
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //如果是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                //如果是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            //插入公共字段
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //插入购物车
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 减少购物车
     * @param shoppingCartDTO
     */

    @Transactional
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        //创建购物车对象
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //设置用户user_id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查看购物车的此次操作对象数量是否大于1
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && list.size() > 0) {
            //如果大于1，执行更新操作，数量减1
            ShoppingCart cart = list.get(0);
            if (cart.getNumber() > 1) {
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(cart);
            } else {
                shoppingCartMapper.deleteById(cart.getId());
            }
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */

    @Override
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.deleteAllByUserId(BaseContext.getCurrentId());
    }
}
