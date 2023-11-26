package com.cloud.mapper;

import com.cloud.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReportMapper {
   @Select("select sum(amount) from orders where status='5' and order_time >= #{begin} and order_time < #{end}")
   Double getAmountByDate(LocalDateTime begin, LocalDateTime end);
   @Select("select count(id) from user where create_time>= #{begin} and create_time < #{end}")
   Integer getUserByDate(LocalDateTime begin, LocalDateTime end);
   @Select("select count(id) from user where create_time<=#{end}")
   Integer getAllUser(LocalDateTime end);
   @Select("select count(id) from orders where status='5' and order_time >= #{begin} and order_time <= #{end}")
   Integer getAllCompletionOrdersOneDay(LocalDateTime begin, LocalDateTime end);
   @Select("select count(id) from orders where order_time >= #{begin} and order_time <= #{end}")
   Integer getAllOrdersOneDay(LocalDateTime begin, LocalDateTime end);
   @Select("select count(id) from orders where status='5'")
   Integer getAllCompletionOrders();
   @Select("select count(id) from orders")
   Integer getAllOrders();

   List<GoodsSalesDTO> getTop10ByDate(LocalDateTime begin, LocalDateTime end);
}
