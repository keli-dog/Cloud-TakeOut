package com.cloud.controller.admin;

import com.cloud.result.Result;
import com.cloud.service.WorkspaceService;
import com.cloud.vo.BusinessDataVO;
import com.cloud.vo.DishOverViewVO;
import com.cloud.vo.OrderOverViewVO;
import com.cloud.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台")
@Slf4j
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;
    @GetMapping("/businessData")
    @ApiOperation("今日运营数据")
    public Result<BusinessDataVO> businessData() {
        log.info("今日运营数据");
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workspaceService.businessData(begin, end);
        return Result.success(businessDataVO);
    }
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> orderOverView(){
        log.info("查询订单管理数据");
        OrderOverViewVO orderOverViewVO = workspaceService.orderOverView();
        return Result.success(orderOverViewVO);
    }
    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> dishOverView(){
        log.info("查询菜品总览");
        return Result.success(workspaceService.getDishOverView());
    }
    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> setmealOverView(){
        log.info("查询套餐总览");
        SetmealOverViewVO setmealOverView = workspaceService.getSetmealOverView();
        return Result.success(setmealOverView);
    }
}
