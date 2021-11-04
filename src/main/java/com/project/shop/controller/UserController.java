package com.project.shop.controller;




import com.project.shop.model.Category;
import com.project.shop.model.Item;
import com.project.shop.model.User;
import com.project.shop.model.dtos.UserDto;
import com.project.shop.model.enums.Status;
import com.project.shop.model.enums.Type;
import com.project.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }
    final  String tmp = "" ;

    @GetMapping("login")
    public String login() {

        return "login";
    }

    @GetMapping("signup")
    public String signUp(Model model) {


        model.addAttribute("userDto", new UserDto());
        return "signup";
    }


//    @GetMapping("shop/item-form")
//    public String listProduct(Model model) {
//        ModelAndView mv = new ModelAndView("shop/item-form");
////        model.addAttribute("category", new Category());
//        model.addAttribute("item", new Item());
//        model.addAttribute("categoryList", categoryService.listCategory());
//        model.addAttribute("itemList", itemService.listItems());
//        return "shop/item-form";
//    }
//
//
//    @PostMapping("shop/item-form")
//    public String addProduct(@Valid Item item, BindingResult bindingResult, User user, @RequestParam("file") MultipartFile file, Model model) {
//        ModelAndView mv = new ModelAndView("shop/item-form");
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("category", new Category());
//            model.addAttribute("categoryList", categoryService.listCategory());
//            model.addAttribute("itemList", itemService.listItems());
//            return "shop/item-form";
//        }
//        model.addAttribute("categoryList", categoryService.listCategory());
//        model.addAttribute("itemList", itemService.listItems());
//        model.addAttribute("message", "Dodano produkt");
//        //          return "redirect:/shop/item-form";//nie widac  potwierdzenia
//        return "/shop/item-form";
//
//    }

    @PostMapping("signup")
    public String signup( @Valid UserDto userDto, BindingResult bindingResult,Model model, @ModelAttribute User user){
        ModelAndView mv = new ModelAndView("signup");

            User tmp;
            tmp = userService.find(userDto.getEmail());
            System.out.println("USer: "+ tmp);
            if(tmp != null){
                model.addAttribute("userDto", new UserDto());
                model.addAttribute("message", "Taki email juz istnieje");
                return "signup";
            } else {

                if (bindingResult.hasErrors()) {

                    return "signup";
                }

                user.setCreatedDate(LocalDateTime.now());
                user.setAccountStatus(Status.AKTYWNE);
                user.setType(Type.USER);
                user.setRole(Type.USER + "");
                userService.save(user);

                model.addAttribute("userDto", new UserDto());


            }
        return "redirect:/login";// przekierowanie na adres metodą GET
    }




//    @GetMapping("error")
//    public String error() {
//        return "error";
//    }







}
