package com.project.shop.dao;


import com.project.shop.model.Category;
import com.project.shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    List<Item> findAllByCategoryId(long categoryId);
    void deleteAllByCategoryId(Category category);
}
