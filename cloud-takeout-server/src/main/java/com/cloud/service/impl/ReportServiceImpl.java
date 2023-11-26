package com.cloud.service.impl;

import com.cloud.dto.GoodsSalesDTO;
import com.cloud.mapper.ReportMapper;
import com.cloud.service.ReportService;
import com.cloud.service.WorkspaceService;
import com.cloud.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    @Override
    public void exportData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.businessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        //通过poi将数据写入xlsx文件
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //创建工作簿
            XSSFWorkbook excel = new XSSFWorkbook(in);
            //获取sheet
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //填充数据(时间段)
            sheet.getRow(1).getCell(1).setCellValue("时间" + begin + "至" + end);
            //获取第四行
            XSSFRow row = sheet.getRow(3);
            //填充数据(营业额)
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            //填充数据(订单完成率)
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            //填充数据(新增用户数)
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());
            //获取第五行
            row = sheet.getRow(4);
            //填充数据(订单完成总数)
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            //填充数据(平均客单价)
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0;i<30;i++){
                LocalDate date = begin.plusDays(i);
                //获取某一天数据
                BusinessDataVO businessDataVO1 = workspaceService.businessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                //获取行
                row = sheet.getRow(7+i);
                //填充数据(日期)
                row.getCell(1).setCellValue(date.toString());
                //填充数据(营业额)
                row.getCell(2).setCellValue(businessDataVO1.getTurnover());
                //填充数据(订单完成总数)
                row.getCell(3).setCellValue(businessDataVO1.getValidOrderCount());
                //填充数据(订单完成率)
                row.getCell(4).setCellValue(businessDataVO1.getOrderCompletionRate());
                //填充数据(平均客单价)
                row.getCell(5).setCellValue(businessDataVO1.getUnitPrice());
                //填充数据(新增用户数)
                row.getCell(6).setCellValue(businessDataVO1.getNewUsers());
            }

            // 导出数据(下载到客户端浏览器)
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            outputStream.close();
            excel.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
