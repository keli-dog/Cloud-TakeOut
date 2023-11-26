package com.cloud.service;

import com.cloud.dto.OrdersPageQueryDTO;
import com.cloud.dto.OrdersPaymentDTO;
import com.cloud.dto.OrdersSubmitDTO;
import com.cloud.result.PageResult;
import com.cloud.vo.OrderPaymentVO;
import com.cloud.vo.OrderStatisticsVO;
import com.cloud.vo.OrderSubmitVO;
import com.cloud.vo.OrderVO;

public interface OrderService {
    //商家端
    OrderSubmitVO createOrder(OrdersSubmitDTO orderSubmitVDTO);
    OrderVO getById(Long id);
    PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO);
    OrderStatisticsVO getStatisticsCount();
    void setStatusAsConfirm(Long id);
    void setStatusAsReject(Long id,String rejectionReason);
    void setStatusAsCancel(Long id,String cancelReason);
    void setStatusAsDelivery(Long id);
    void setStatusAsComplete(Long id);

    //用户端
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
    void paySuccess(String outTradeNo);
    void repetitionOrder(Long id);
    PageResult pageQueryForUser(int page, int pageSize, Integer status);
    void userCancelById(Long id) throws Exception;
    void reminder(Long id);


}
