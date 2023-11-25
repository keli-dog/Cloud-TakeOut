package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        // 添加开始时间
        dateList.add(begin);
        // 循环获取日期
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 获取每天营业额
        List<Double> turnoverList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime begintime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endtime = LocalDateTime.of(date, LocalTime.MAX);
            Double amountByDate = reportMapper.getAmountByDate(begintime, endtime);
            amountByDate = amountByDate == null ? 0.0 : amountByDate;
            turnoverList.add(amountByDate);
        });
        // 转化为字符串
        String datelist = StringUtils.join(dateList, ",");
        String turnoverlist = StringUtils.join(turnoverList, ",");
        // 封装VO
        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(datelist)
                .turnoverList(turnoverlist)
                .build();
        return turnoverReportVO;
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        // 添加开始时间
        dateList.add(begin);
        // 循环获取日期
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 获取每天新用户和总用户
        List<Integer> newuserList = new ArrayList<>();
        List<Integer> alluserList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime begintime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endtime = LocalDateTime.of(date, LocalTime.MAX);
            // 获取新用户
            Integer userByDate = reportMapper.getUserByDate(begintime, endtime);
            userByDate = userByDate == null ? 0 : userByDate;
            newuserList.add(userByDate);
            // 获取总用户
            Integer allUser = reportMapper.getAllUser(endtime);
            allUser = allUser == null ? 0 : allUser;
            alluserList.add(allUser);
        });
        // 转化为字符串
        String datelist = StringUtils.join(dateList, ",");
        String newuserlist = StringUtils.join(newuserList, ",");
        String alluserlist = StringUtils.join(alluserList, ",");
        // 封装VO
        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(datelist)
                .newUserList(newuserlist)
                .totalUserList(alluserlist)
                .build();
        return userReportVO;
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        // 添加开始时间
        dateList.add(begin);
        // 循环获取日期
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> allCompletionOrdersOneDaylist = new ArrayList<>();
        List<Integer> allOrdersOneDaylist = new ArrayList<>();
        // 循环获取每天订单数量和完成订单数量
        dateList.forEach(date -> {
            LocalDateTime begintime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endtime = LocalDateTime.of(date, LocalTime.MAX);
            //获取今日完成订单总数
            Integer allCompletionOrdersOneDay = reportMapper.getAllCompletionOrdersOneDay(begintime, endtime);
            allCompletionOrdersOneDay = allCompletionOrdersOneDay == null ? 0 : allCompletionOrdersOneDay;
            allCompletionOrdersOneDaylist.add(allCompletionOrdersOneDay);
            // 获取今日订单总数
            Integer allOrdersOneDay = reportMapper.getAllOrdersOneDay(begintime, endtime);
            allOrdersOneDay = allOrdersOneDay == null ? 0 : allOrdersOneDay;
            allOrdersOneDaylist.add(allOrdersOneDay);

        });
        // 获取完成订单总数
        Integer allCompletionOrders = reportMapper.getAllCompletionOrders();
        allCompletionOrders = allCompletionOrders == null ? 0 : allCompletionOrders;
        // 获取订单总数
        Integer allOrders = reportMapper.getAllOrders();
        allOrders = allOrders == null ? 0 : allOrders;
        // 获取订单完成率
        Double orderCompletionRateOneDay = allCompletionOrders * 1.0 / allOrders;
        orderCompletionRateOneDay = orderCompletionRateOneDay == null ? 0.0 : orderCompletionRateOneDay;
        // 转化为字符串
        String datelist = StringUtils.join(dateList, ",");
        String allCompletionOrdersOneDaylistStr = StringUtils.join(allCompletionOrdersOneDaylist, ",");
        String allOrdersOneDaylistStr = StringUtils.join(allOrdersOneDaylist, ",");
        //封装VO
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(datelist)
                .validOrderCountList(allCompletionOrdersOneDaylistStr)
                .orderCountList(allOrdersOneDaylistStr)
                .orderCompletionRate(orderCompletionRateOneDay)
                .totalOrderCount(allOrders)
                .validOrderCount(allCompletionOrders)
                .build();
        return orderReportVO;

    }

    @Override
    public SalesTop10ReportVO top10Statistics(LocalDate begin, LocalDate end) {

        // 获取日期
        LocalDateTime begintime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endtime = LocalDateTime.of(end, LocalTime.MAX);
        //查询销量前10的菜品
        List<GoodsSalesDTO> goodsSalesDTOList = reportMapper.getTop10ByDate(begintime, endtime);
        //
        List<String> nameList = new ArrayList<>();
        List<String> numberList = new ArrayList<>();
        if (goodsSalesDTOList != null && goodsSalesDTOList.size() > 0) {
            goodsSalesDTOList.forEach(goodsSalesDTO -> {
                nameList.add(goodsSalesDTO.getName());
                numberList.add(goodsSalesDTO.getNumber().toString());
            });
        } else {
            nameList.add("暂无数据");
            numberList.add("0");
        }
        // 封装VO
        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();

        return salesTop10ReportVO;
    }
}
