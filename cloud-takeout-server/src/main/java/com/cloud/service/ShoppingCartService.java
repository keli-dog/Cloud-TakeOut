package com.cloud.service;

import com.cloud.dto.ShoppingCartDTO;
import com.cloud.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void add(ShoppingCartDTO shoppingCartDTO);
    void sub(ShoppingCartDTO shoppingCartDTO);
    List<ShoppingCart> showShoppingCart();
    void clean();


}
