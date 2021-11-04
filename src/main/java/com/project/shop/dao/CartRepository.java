package com.project.shop.dao;

import com.project.shop.model.Cart;
import com.project.shop.model.Item;
import com.project.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

//    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
      List<Cart> findCartsByUser(User user);

      void deleteCartByUser(User user);
      void deleteByUser(User user);
      void deleteCartByItem(Item item);
}
