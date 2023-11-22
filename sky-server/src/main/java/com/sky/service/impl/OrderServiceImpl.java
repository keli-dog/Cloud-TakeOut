package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void setStatusAsReject(Long id,String rejectionReason) {
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
                ||orders.getStatus() == Orders.DELIVERY_IN_PROGRESS
                ||orders.getStatus() == Orders.COMPLETED) {
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


    Orders setAddressBook(Orders orders) {
        //设置收件人信息
        AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() +
                "--" + addressBook.getCityName() +
                "--" + addressBook.getDistrictName()+
                "--"+addressBook.getDetail());
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


    /* TODO 以下代码无法使用*/

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

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

        orderMapper.update(orders);
    }

}
