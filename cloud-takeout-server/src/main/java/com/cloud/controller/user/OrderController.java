package com.cloud.controller.user;

import com.cloud.dto.OrdersPaymentDTO;
import com.cloud.dto.OrdersSubmitDTO;
import com.cloud.result.PageResult;
import com.cloud.result.Result;
import com.cloud.service.OrderService;
import com.cloud.vo.OrderPaymentVO;
import com.cloud.vo.OrderSubmitVO;
import com.cloud.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("创建订单")
    public Result<OrderSubmitVO> create(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("创建订单,{}" + ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.createOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("查询订单详情");
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 历史订单查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> page(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQueryForUser(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
        log.info("支付成功");
        return Result.success(orderPaymentVO);
    }
    /**
     * 用户取消订单
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetitionOrder(@PathVariable("id") Long id){
        log.info("再来一单{}",id);
        orderService.repetitionOrder(id);
        return Result.success();
    }
    @GetMapping("reminder/{id}")
    @ApiOperation("催单")
    public Result getStatisticsCount(@PathVariable("id") Long id){
        log.info("催单");
      orderService.reminder(id);
      return Result.success();
    }



}
