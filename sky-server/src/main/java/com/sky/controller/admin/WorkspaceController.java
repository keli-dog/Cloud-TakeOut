package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        BusinessDataVO businessDataVO = workspaceService.businessData();
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
