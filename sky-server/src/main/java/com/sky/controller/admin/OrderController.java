package com.sky.controller.admin;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/details/{id}")
    @ApiOperation("根据id查询订单详情")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("根据id查询订单详情");
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    @GetMapping("/conditionSearch")
    @ApiOperation("根据条件查询订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("根据条件查询订单", ordersPageQueryDTO);
        PageResult orderVO = orderService.getPage(ordersPageQueryDTO);
        return Result.success(orderVO);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> getStatisticsCount() throws Exception {
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.getStatisticsCount();
        return Result.success(orderStatisticsVO);
    }

    @PutMapping(value="/confirm")
    @ApiOperation("接单")
    public Result setStatusAsConfirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单");
        orderService.setStatusAsConfirm(ordersConfirmDTO.getId());
        return Result.success(LocalDateTime.now());
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result setStatusAsRejection(@RequestBody OrdersRejectionDTO orderRejectionDTO) {
        log.info("拒单"+orderRejectionDTO);
        orderService.setStatusAsReject(orderRejectionDTO.getId(),orderRejectionDTO.getRejectionReason());
        return Result.success(LocalDateTime.now());
    }
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result setStatusAsCancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单"+ordersCancelDTO);
        orderService.setStatusAsCancel(ordersCancelDTO.getId(),ordersCancelDTO.getCancelReason());
        return Result.success(LocalDateTime.now());
    }
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result setStatusAsDelivery(@PathVariable("id") Long id) {
        log.info("派送订单"+id);
        orderService.setStatusAsDelivery(id);
        return Result.success(LocalDateTime.now());
    }
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result setStatusAsComplete(@PathVariable("id")Long id) {
        log.info("完成订单"+id);
        orderService.setStatusAsComplete(id);
        return Result.success(LocalDateTime.now());
    }


}
