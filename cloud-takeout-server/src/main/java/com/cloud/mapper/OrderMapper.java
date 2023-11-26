package com.cloud.mapper;

import com.github.pagehelper.Page;
import com.cloud.dto.OrdersPageQueryDTO;
import com.cloud.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态和时间查询订单
     * @param status
     * @param overtime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{overtime}")
    List<Orders> getByStatusAndOerderTime(Integer status, LocalDateTime overtime);
}
