package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {
        //获得当天新增用户
        Integer newUsers = workspaceMapper.getUserTodayCount(begin, end);
        newUsers = newUsers==null? 0 : newUsers;
        //获得订单总数
        Integer all = workspaceMapper.OrderAllCount(begin, end);
        all = all==null? 0 : all;
        //获得订单完成数
        Integer completed = workspaceMapper.orderCompletionCount(begin, end);
        completed = completed==null? 0 : completed;
        //获得订单完成率
        Double orderCompletionRate = (double) completed / all;
        orderCompletionRate = orderCompletionRate==null? 0.0 : orderCompletionRate;
       //获得订单完成金额
        Double turnover = workspaceMapper.turnover(begin, end);
        turnover = turnover==null ? 0.0 : turnover;
        //获得订单平均单价
        Double unitPrice = turnover / completed;
        unitPrice = unitPrice==null? 0.0 : unitPrice;
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(completed)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
        return businessDataVO;

    }
    @Override
    public OrderOverViewVO orderOverView() {
        List<Orders> list = orderMapper.list();
        int toBeConfirmed = 0;//待接单
        int waitingOrders = 0;//待派送
        int deliveryInProgress = 0;//派送中
        int completed = 0;//已完成
        int cancelled = 0;//已取消
        for (Orders orders : list) {
            if (orders.getStatus() == Orders.TO_BE_CONFIRMED) {
                toBeConfirmed++;
            } else if (orders.getStatus() == Orders.CONFIRMED) {
                waitingOrders++;
            } else if (orders.getStatus() == Orders.DELIVERY_IN_PROGRESS) {
                deliveryInProgress++;
            }else if (orders.getStatus() == Orders.COMPLETED) {
                completed++;
            }else if (orders.getStatus() == Orders.CANCELLED) {
                cancelled++;
            }
        }

        //创建VO
        OrderOverViewVO orderOverViewVO = new OrderOverViewVO();
        orderOverViewVO.setCancelledOrders(cancelled);
        orderOverViewVO.setCompletedOrders(completed);
        orderOverViewVO.setWaitingOrders(waitingOrders);
        orderOverViewVO.setWaitingOrders(toBeConfirmed);
        orderOverViewVO.setDeliveredOrders(deliveryInProgress);
        return orderOverViewVO;
    }

    @Override
    public DishOverViewVO getDishOverView() {
        List<Dish> dishList = dishMapper.list();
        List<Setmeal> setmealList = setmealMapper.list();
        Integer dishOnSale = 0;
        Integer dishDiSale = 0;
        Integer setmealOnSale = 0;
        Integer setmealDiSale = 0;
        for (Dish dish : dishList) {
            if (dish.getStatus() == 1) {
                dishOnSale++;
            }else if (dish.getStatus() == 0) {
                dishDiSale++;
            }
        }
        for (Setmeal setmeal : setmealList) {
            if (setmeal.getStatus() == 1) {
                setmealOnSale++;
            }else if (setmeal.getStatus() == 0) {
                setmealDiSale++;
            }
        }
        DishOverViewVO dishOverViewVO = new DishOverViewVO();
        dishOverViewVO.setSold(dishOnSale);
        dishOverViewVO.setDiscontinued(dishDiSale);
        return dishOverViewVO;
    }
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        List<Setmeal> setmealList = setmealMapper.list();
        Integer setmealOnSale = 0;
        Integer setmealDiSale = 0;
        for (Setmeal setmeal : setmealList) {
            if (setmeal.getStatus() == 1) {
                setmealOnSale++;
            }else if (setmeal.getStatus() == 0) {
                setmealDiSale++;
            }
        }
        SetmealOverViewVO setmealOverViewVO = new SetmealOverViewVO();
        setmealOverViewVO.setSold(setmealOnSale);
        setmealOverViewVO.setDiscontinued(setmealDiSale);
        return setmealOverViewVO;
    }
}
