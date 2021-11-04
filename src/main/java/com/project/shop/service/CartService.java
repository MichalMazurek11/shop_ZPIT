package com.project.shop.service;

import com.project.shop.model.Cart;
import com.project.shop.model.Item;
import com.project.shop.model.User;

import java.util.List;

public interface CartService {



    public void save(Cart cart);

    public List<Cart> findCartsByUser(User user);

    void deleteByUser(User user);

    void deleteCartByItem(Item item);


    void deleteCartByUser(User user);
}
