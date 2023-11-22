package com.sky.service.impl;

import com.sky.mapper.UserMapper;
import com.sky.mapper.WorkspaceMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public BusinessDataVO businessData() {
        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 今天的开始时间（0:00）
        LocalDateTime startOfToday = today.atStartOfDay();

        // 今天的结束时间（23:59:59.999999999）
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);
        System.out.println(startOfToday);
        System.out.println(endOfToday);


        Integer newUsers = workspaceMapper.getUserTodayCount(startOfToday, endOfToday);
        Integer all = workspaceMapper.OrderAllCount();
        Integer completed = workspaceMapper.orderCompletionCount();
        double orderCompletionRate = (double) completed / all;

        double turnover = workspaceMapper.turnover(startOfToday, endOfToday);
        double unitPrice = turnover / completed;
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(completed)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
        return businessDataVO;

    }
}
