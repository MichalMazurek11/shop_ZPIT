package com.project.shop.service;


import com.project.shop.dao.CartRepository;
import com.project.shop.model.Cart;
import com.project.shop.model.Item;
import com.project.shop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements  CartService {

    @Autowired
    private CartRepository cartRepository;



    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);

    }

    @Override
    public List<Cart> findCartsByUser(User user) {
        return cartRepository.findCartsByUser(user);
    }

    @Override
    public void deleteByUser(User user) {
        cartRepository.deleteByUser(user);
    }

    @Override
    public void deleteCartByItem(Item item) {
        cartRepository.deleteCartByItem(item);
    }

    @Override
    public void deleteCartByUser(User user) {
        cartRepository.deleteCartByUser(user);
    }


}


