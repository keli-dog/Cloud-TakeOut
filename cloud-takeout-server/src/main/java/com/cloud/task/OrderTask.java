package com.cloud.task;

import com.cloud.entity.Orders;
import com.cloud.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时处理超时订单
     */

    @Scheduled(cron = "0 0 0 * * ? ")//每隔1分钟执行一次
    public void orderTask() {
        log.info("定时处理超时订单，{}", LocalDateTime.now());
        //当前时间-15分钟
        LocalDateTime overtime = LocalDateTime.now().plusMinutes(-15);
        //查询超时订单
        List<Orders> orderlist =
                orderMapper.getByStatusAndOerderTime(Orders.PENDING_PAYMENT, overtime);
        if (orderlist != null || orderlist.size() > 0) {
            //修改订单状态
            for (Orders orders : orderlist) {
                log.info("订单id：{}，超时时间：{}", orders.getId(), LocalDateTime.now());
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("超时未支付");
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 定时处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")//每天1点执行一次
    public void orderTask2() {
        log.info("定时处理一直处于派送中的订单，{}", LocalDateTime.now());
        //当前时间-1小时(处理上一个工作日的派送中订单)
        LocalDateTime overtime = LocalDateTime.now().plusHours(-1);
        //查询派送中订单
        List<Orders> orderlist =
                orderMapper.getByStatusAndOerderTime(Orders.DELIVERY_IN_PROGRESS, overtime);
        if (orderlist != null || orderlist.size() > 0) {
            //修改订单状态
            for (Orders orders : orderlist) {
                log.info("订单id：{}，已完成", orders.getId());
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
