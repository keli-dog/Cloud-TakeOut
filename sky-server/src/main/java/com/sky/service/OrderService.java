package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO createOrder(OrdersSubmitDTO orderSubmitVDTO);
    OrderVO getById(Long id);
    PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO);
    OrderStatisticsVO getStatisticsCount();
    void setStatusAsConfirm(Long id);
    void setStatusAsReject(Long id,String rejectionReason);
    void setStatusAsCancel(Long id,String cancelReason);
    void setStatusAsDelivery(Long id);
    void setStatusAsComplete(Long id);
/**/
    //TODO 以下代码无法使用
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
    void paySuccess(String outTradeNo);


}
