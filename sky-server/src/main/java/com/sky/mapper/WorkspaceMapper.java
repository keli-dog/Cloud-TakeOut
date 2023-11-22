package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface WorkspaceMapper {
    @Select("select count(1) from orders where status='5'")
    Integer orderCompletionCount();
    @Select("select count(1) from orders")
    Integer OrderAllCount();
    @Select("select count(1) from user where create_time between #{begin} and #{end}")
    Integer getUserTodayCount(LocalDateTime begin, LocalDateTime end);
    @Select("select sum(amount) from orders where status='5' and order_time >= #{begin} and delivery_time <= #{end}")
    Double turnover(LocalDateTime begin, LocalDateTime end);
}
