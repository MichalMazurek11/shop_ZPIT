package com.project.shop.config;

import com.project.shop.dao.UserRepository;
import com.project.shop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserPrincipalDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);
		UserPrincipal userPrincipal = new UserPrincipal(user);
		System.out.println("Znaleziony uzytkownik: "+ userPrincipal.getUsername() + " haslo: "+ userPrincipal.getPassword());
		return userPrincipal;
	}
}
