package com.cloud.service;

import com.cloud.vo.BusinessDataVO;
import com.cloud.vo.DishOverViewVO;
import com.cloud.vo.OrderOverViewVO;
import com.cloud.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);
    OrderOverViewVO orderOverView();
    DishOverViewVO getDishOverView();
    SetmealOverViewVO getSetmealOverView();
}
