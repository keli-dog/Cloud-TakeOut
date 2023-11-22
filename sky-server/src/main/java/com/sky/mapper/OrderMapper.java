package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
    Page<Orders> getPage(OrdersPageQueryDTO ordersPageQueryDTO);
    @Select("select * from orders")
    List<Orders> list();

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);
    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
}
