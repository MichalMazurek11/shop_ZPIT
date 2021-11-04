package com.project.shop.service;

import com.project.shop.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {

    public User findByEmail(String email);

    public void save(User user);

//    public void update(User user);

    public List<User> findAllUser();

    public void deleteUser(long userId);

    public boolean emailExist(String email) ;

    public User  find(String email);
//    public boolean findEmail(String email);

    void deleteByEmail(String email);


}