package com.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface WorkspaceMapper {
    @Select("select count(id) from orders where status='5' and order_time >= #{begin} and order_time <= #{end}")
    Integer orderCompletionCount(LocalDateTime begin, LocalDateTime end);
    @Select("select count(id) from orders where order_time >= #{begin} and order_time <= #{end}")
    Integer OrderAllCount(LocalDateTime begin, LocalDateTime end);
    @Select("select count(id) from user where create_time between #{begin} and #{end}")
    Integer getUserTodayCount(LocalDateTime begin, LocalDateTime end);
    @Select("select sum(amount) from orders where status='5' and order_time >= #{begin} and order_time <= #{end}")
    Double turnover(LocalDateTime begin, LocalDateTime end);
}
