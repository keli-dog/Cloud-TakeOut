package com.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cloud.constant.MessageConstant;
import com.cloud.context.BaseContext;
import com.cloud.dto.OrdersPageQueryDTO;
import com.cloud.dto.OrdersPaymentDTO;
import com.cloud.dto.OrdersSubmitDTO;
import com.cloud.entity.*;
import com.cloud.exception.AddressBookBusinessException;
import com.cloud.exception.OrderBusinessException;
import com.cloud.exception.ShoppingCartBusinessException;
import com.cloud.mapper.*;
import com.cloud.result.PageResult;
import com.cloud.service.OrderService;
import com.cloud.utils.WeChatPayUtil;
import com.cloud.vo.OrderPaymentVO;
import com.cloud.vo.OrderStatisticsVO;
import com.cloud.vo.OrderSubmitVO;
import com.cloud.vo.OrderVO;
import com.cloud.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    @Transactional
    public OrderSubmitVO createOrder(OrdersSubmitDTO orderSubmitVDTO) {

        //判断地址是否为空
        AddressBook addressBook = addressBookMapper.getById(orderSubmitVDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //判断购物车是否为空
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartLlist = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartLlist == null && shoppingCartLlist.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //插入订单表
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitVDTO, orders);
        orders.setUserId(BaseContext.getCurrentId());//设置订单用户id
        orders.setOrderTime(LocalDateTime.now());//设置订单创建时间
        orders.setStatus(Orders.PENDING_PAYMENT);//设置订单状态待支付
        orders.setPayStatus(Orders.UN_PAID);//设置支付状态未支付
        orders.setNumber(BaseContext.getCurrentId() + ":" + System.currentTimeMillis());//设置订单编号
        orders.setConsignee(addressBook.getConsignee());//设置订单收货人
        orders.setPhone(addressBook.getPhone());//设置订单联系电话
        orderMapper.insert(orders);

        //插入订单详情表
        shoppingCartLlist.forEach(shoppingcart -> {
            OrderDetail ordersDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingcart, ordersDetail);
            ordersDetail.setOrderId(orders.getId());
            orderDetailMapper.insert(ordersDetail);
        });

        //清空购物车
        shoppingCartMapper.deleteAllByUserId(BaseContext.getCurrentId());

        //返回订单信息
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }

    @Override
    public OrderVO getById(Long id) {
        //查询订单信息
        Orders orders = orderMapper.getById(id);

        //查询订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        ///设置收件人信息
        orders = setAddressBook(orders);

        //获取菜品信息表
        List<String> dishes = setOrderDishes(orderDetails);

        //构建订单VO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        orderVO.setOrderDishes(dishes.toString());
        return orderVO;
    }

    @Override
    public PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        //开启分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //查询订单列表
        Page<Orders> page = orderMapper.getPage(ordersPageQueryDTO);
        //关闭分页查询
        PageHelper.clearPage();

        //创建VO列表
        List<OrderVO> orderVOList = new ArrayList<>();

        //遍历订单列表
        page.forEach(orders -> {
            //查询订单详情
            List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());

            //设置收件人信息
            orders = setAddressBook(orders);

            //获取菜品信息表
            List<String> dishes = setOrderDishes(orderDetails);

            //构建订单VO
            OrderVO ordersVO = new OrderVO();
            BeanUtils.copyProperties(orders, ordersVO);
            ordersVO.setOrderDetailList(orderDetails);
            ordersVO.setOrderDishes(dishes.toString());

            //将VO对象保存到订单VO列表
            orderVOList.add(ordersVO);
        });

        //封装分页结果
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(orderVOList);
        return pageResult;

    }

    Orders setAddressBook(Orders orders) {
        //设置收件人信息
        AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() +
                "--" + addressBook.getCityName() +
                "--" + addressBook.getDistrictName() +
                "--" + addressBook.getDetail());
        return orders;
    }

    List<String> setOrderDishes(List<OrderDetail> orderDetails) {
        //创建菜品信息表
        List<String> dishList = new ArrayList<>();

        //遍历订单详情，将菜品信息添加到菜品信息表中
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getDishId() != null) {
                Dish dish = dishMapper.getById(orderDetail.getDishId());
                dishList.add(dish.getName() + "*" + orderDetail.getNumber());
            } else {
                Setmeal setmeal = setmealMapper.getById(orderDetail.getSetmealId());
                dishList.add(setmeal.getName());
            }
        }

        return dishList;
    }

    @Override
    public OrderStatisticsVO getStatisticsCount() {
        List<Orders> list = orderMapper.list();
        int toBeConfirmed = 0;//待接单
        int confirmed = 0;//待取餐
        int deliveryInProgress = 0;//派送中
        for (Orders orders : list) {
            if (orders.getStatus() == Orders.TO_BE_CONFIRMED) {
                toBeConfirmed++;
            } else if (orders.getStatus() == Orders.CONFIRMED) {
                confirmed++;
            } else if (orders.getStatus() == Orders.DELIVERY_IN_PROGRESS) {
                deliveryInProgress++;
            }
        }

        //创建VO
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }


    @Override
    public void setStatusAsConfirm(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() == Orders.TO_BE_CONFIRMED) {
            orders.setStatus(Orders.CONFIRMED);
            orders.setEstimatedDeliveryTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }

    @Override
    public void setStatusAsReject(Long id, String rejectionReason) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() == Orders.TO_BE_CONFIRMED) {
            orders.setStatus(Orders.REJECTED);
            orders.setRejectionReason(rejectionReason);
            orderMapper.update(orders);
        }
    }

    @Override
    public void setStatusAsCancel(Long id, String cancelReason) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() == Orders.CONFIRMED
                || orders.getStatus() == Orders.DELIVERY_IN_PROGRESS
                || orders.getStatus() == Orders.COMPLETED) {
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason(cancelReason);
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }


    @Override
    public void setStatusAsDelivery(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() == Orders.CONFIRMED) {
            orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
            orders.setDeliveryStatus(1);
            orders.setEstimatedDeliveryTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }

    @Override
    public void setStatusAsComplete(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() == Orders.DELIVERY_IN_PROGRESS) {
            orders.setStatus(Orders.COMPLETED);
            orders.setDeliveryTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        /*// 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "云端外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));*/
        Orders orders = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        if (orders.getStatus() == Orders.CANCELLED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 跳过预支付，直接返回一个vo
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setTimeStamp("vo.getTimeStamp()");
        vo.setNonceStr("vo.getNonceStr()");
        vo.setPaySign("vo.getPaySign()");
        vo.setPackageStr("vo.getPackageStr()");

        // 支付成功后，更新订单状态
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return vo;
    }

    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        // 更新订单
        orderMapper.update(orders);

        //构建消息
        Map msp = new HashMap();
        msp.put("type", 1);// 1 来单提醒 2 客户催单
        msp.put("orderId", ordersDB.getId());
        msp.put("content", "订单号：" + ordersDB.getNumber());
        String jsonString = JSON.toJSONString(msp);
        // 给商家端推送新订单消息消息
        webSocketServer.sendToAllClient(jsonString);
    }

    @Override
    public void repetitionOrder(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象添加到数据库
        shoppingCartList.forEach(shoppingCart -> {
            shoppingCartMapper.insert(shoppingCart);
        });
    }

    @Override
    public PageResult pageQueryForUser(int page, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> pages = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (pages != null && pages.getTotal() > 0) {
            for (Orders orders : pages) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(pages.getTotal(), list);
    }

    public void userCancelById(Long id) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
           /* weChatPayUtil.refund(
                    ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额
            */
            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        // 校验订单是否存在
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //构建消息
        Map map = new HashMap();
        map.put("type", 2);// 1 来单提醒 2 客户催单
        map.put("orderId", id);
        map.put("content", "客户催单啦，订单号：" + orders.getNumber());
        String jsonString = JSON.toJSONString(map);
        // 给商家端推送客户催单消息
        webSocketServer.sendToAllClient(jsonString);
    }
}
