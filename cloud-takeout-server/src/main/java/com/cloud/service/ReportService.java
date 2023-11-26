package com.cloud.service;

import com.cloud.vo.OrderReportVO;
import com.cloud.vo.SalesTop10ReportVO;
import com.cloud.vo.TurnoverReportVO;
import com.cloud.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin , LocalDate end);
    UserReportVO userStatistics(LocalDate begin, LocalDate end);
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
    SalesTop10ReportVO top10Statistics(LocalDate begin, LocalDate end);
    void exportData(HttpServletResponse response);
}
