package com.project.shop.controller;

import com.project.shop.model.*;
import com.project.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;


    @GetMapping("shop/home")
    public String home(@ModelAttribute User user) {


        return "shop/home";
    }

    @GetMapping("shop/admin/delete-User")
    public String adminMode(@ModelAttribute User user, Model model) {

        model.addAttribute("user", new User());
        return "admin/index";
    }


    @RequestMapping(value = {"/shop/admin/delete-User/", "/shop/admin/delete-User/{email}"})
    public String deleteUser(@RequestParam(value = "email", required = false)String email) {


        userService.deleteByEmail(email);
        return "admin/index";
    }


    @GetMapping({"shop/index", "/"})
    public String index(Principal prin, Model model) {




        model.addAttribute("categoryList", categoryService.listCategory());
        model.addAttribute("itemList", itemService.findItemsByStatus("AKTYWNA"));
        return "shop/index";
    }

    @GetMapping("shop/category-form")
    public ModelAndView listCategory() {
        ModelAndView mv = new ModelAndView("shop/category-form");
        mv.addObject("categoryList", categoryService.listCategory());
        return mv;
    }

    @PostMapping("shop/category-form/add-category")
    public ModelAndView addCategory(Category category) {
        ModelAndView mv = new ModelAndView("shop/category-form");
        categoryService.addCategory(category);
        mv.addObject("categoryList", categoryService.listCategory());
        return mv;
    }
    @GetMapping("shop/category-form/delete-Category/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId")String categoryId) {
        ModelAndView mv = new ModelAndView("shop/category-form");

        categoryService.deleteCategory(Long.parseLong(categoryId));
        mv.addObject("categoryList", categoryService.listCategory());
        return "redirect:/shop/category-form";
    }


    @GetMapping("shop/items")
    public String allProduct(Model model, Authentication auth) {




        model.addAttribute("itemList", itemService.findItemsByStatus("AKTYWNA"));
        model.addAttribute("categoryList", categoryService.listCategory());
        return "shop/index";
    }

    @GetMapping("shop/getProducts/{categoryId}")
    public ModelAndView getProductFromCategory(@PathVariable("categoryId")String categoryId) {
        ModelAndView mv = new ModelAndView("shop/index");
        long categoryLongId = Long.parseLong(categoryId);


        mv.addObject("itemList",itemService.findByCategory_CategoryIdAndStatus(categoryLongId,"AKTYWNA"));
        mv.addObject("categoryList", categoryService.listCategory());
        return mv;
    }


    //find itemByName
    @GetMapping("shop/getProducts/itemName{itemName}")
    public ModelAndView getProductFromName(@PathVariable("itemName")String itemName) {
        ModelAndView mv = new ModelAndView("shop/index");


        if(itemName != null){
            mv.addObject("itemList",itemService.findItemsByItemNameAndStatus(itemName,"AKTYWNA"));
            mv.addObject("categoryList", categoryService.listCategory());
        }else{
            mv.addObject("itemList",itemService.findItemsByStatus("AKTYWNA"));
            mv.addObject("categoryList", categoryService.listCategory());
        }


        return mv;
    }


    @GetMapping("shop/itemName")
    public ModelAndView getProductName(String itemName) {
        ModelAndView mv = new ModelAndView("shop/index");


      System.out.println(itemName.equals(""));
        if(itemName != null ) {
            mv.addObject("itemList", itemService.findItemsByItemNameAndStatus(itemName, "AKTYWNA"));
            mv.addObject("categoryList", categoryService.listCategory());
        }
       if(  Objects.equals(itemName,"")){
            mv.addObject("itemList",itemService.findItemsByStatus("AKTYWNA"));
            mv.addObject("categoryList", categoryService.listCategory());
        }


        return mv;
    }

    @PostMapping("shop/purchase/{itemId}")
    public String toPurchaseItem(@PathVariable("itemId")String itemId,Authentication auth,User user) {

        long itemLongId = Long.parseLong(itemId);
       Item item = itemService.findItemByItemId(itemLongId);
       item.setStatus("NIEAKTYWNA");
       itemService.save(item);
       System.out.println(itemLongId);

        String login = auth.getName();
        user = userService.find(login);

        return "redirect:/shop/index";
    }



    @GetMapping("shop/item-form")
    public String listProduct(Model model) {
        ModelAndView mv = new ModelAndView("shop/item-form");
//        model.addAttribute("category", new Category());
        model.addAttribute("item", new Item());
        model.addAttribute("categoryList", categoryService.listCategory());
        model.addAttribute("itemList", itemService.listItems());
        return "shop/item-form";
    }


    @PostMapping("shop/item-form")
    public String addProduct(@Valid Item item, BindingResult bindingResult, User user, @RequestParam("file") MultipartFile file, Model model) {
        ModelAndView mv = new ModelAndView("shop/item-form");

        if (bindingResult.hasErrors()) {
            model.addAttribute("category", new Category());
            model.addAttribute("categoryList", categoryService.listCategory());
            model.addAttribute("itemList", itemService.listItems());
            return "shop/item-form";
        }
            item.setImage("\\resources\\" + file.getOriginalFilename());

            itemService.addItem(item);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();
            user = userService.find(login);
            item.setUser(user);
            itemService.save(item);


        model.addAttribute("categoryList", categoryService.listCategory());
        model.addAttribute("itemList", itemService.listItems());
        model.addAttribute("message", "Dodano produkt");
   //          return "redirect:/shop/item-form";//nie widac  potwierdzenia
        return "/shop/item-form";

    }

    @PostMapping("/shop/add/{itemId}/ilosc/{quantity}")
    public String addToCart( @PathVariable("itemId")String itemId,@RequestParam Map<String, String> requestParams, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userService.find(login);
        long itemLongId = Long.parseLong(itemId);
        Item item2 = itemService.findItemByItemId(itemLongId);
        String tmpQuantity = requestParams.get("quantity");
        int quantityCart = Integer.parseInt(tmpQuantity);

        Cart cart = new Cart(item2,quantityCart ,user);
        cartService.save(cart);


        model.addAttribute("item", new Item());
        model.addAttribute("categoryList", categoryService.listCategory());
        model.addAttribute("itemList", itemService.listItems());
        return "redirect:/shop/index";


    }


//    @GetMapping("/shop/add/{itemId}/ilosc/{quantity}")
//    public String test( @RequestParam(value = "itemId",required = false)String itemId,@RequestParam(value = "quantity")String quantity, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        long itemLongId = Long.parseLong(itemId);
//        Item item2 = itemService.findItemByItemId(itemLongId);
//        System.out.println("ITEMID : "+ itemId);
//        System.out.println("ILOSC : " + quantity);
//
//
//        model.addAttribute("item", new Item());
//        model.addAttribute("categoryList", categoryService.listCategory());
//        model.addAttribute("itemList", itemService.listItems());
//        return "redirect:/shop/index";
//
//
//    }



    @GetMapping("/shop/shopingCart/delete/")
    public ModelAndView deleteFromCart( Model model)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userService.find(login);
        ModelAndView mv = new ModelAndView("shop/shopingCart");


        cartService.deleteCartByUser(user);
        float total = 0 ;
        mv.addObject("total",total);
        model.addAttribute("item", new Item());
        model.addAttribute("categoryList", categoryService.listCategory());
        model.addAttribute("itemList", itemService.listItems());
        return mv;
    }

    @GetMapping("shop/shopingCart/purchase")
    public ModelAndView purchaseFromCart( Model model)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userService.find(login);
        ModelAndView mv = new ModelAndView("shop/shopingCart");
        float total = 0;


        List<Cart> list = cartService.findCartsByUser(user);
        if(list.size() == 0 ){
            mv.addObject("total", total);

            model.addAttribute("message2", "Musisz dodac cos do koszyka");
            model.addAttribute("item", new Item());
            model.addAttribute("categoryList", categoryService.listCategory());
            model.addAttribute("itemList", itemService.listItems());

            return mv;
        }else{
            total = findSumCart(user);
            LocalDateTime localDateTime = LocalDateTime.now();
            String text = "Dziękujemy za zakup w naszym sklepie, do zapłaty masz "+ total +" zł. Zapraszamy ponownie :)";
            mailService.sendEmail(user.getEmail(),"Zakup "+ localDateTime,text);

            cartService.deleteCartByUser(user);
            total = 0;
            mv.addObject("total", total);
            model.addAttribute("message", "Dziękujemy za zamówienie");
            model.addAttribute("item", new Item());
            model.addAttribute("categoryList", categoryService.listCategory());
            model.addAttribute("itemList", itemService.listItems());
            return mv;
        }




    }


    @GetMapping("shop/shopingCart")
    public ModelAndView shopingCart(Authentication auth, Model model) {
        ModelAndView mv = new ModelAndView("shop/shopingCart");

        String login = auth.getName();
        User user = userService.find(login);

        float total = 0;
        total = findSumCart(user);
        cartService.findCartsByUser(user);

        mv.addObject("total", total);

        List<Cart> cartList = cartService.findCartsByUser(user);
        model.addAttribute("cartList", cartList);


        return mv;
    }

    private float findSumCart(User user){
        List<Cart> list = cartService.findCartsByUser(user);
        float sum = 0;
        for(int i=0; i<list.size(); i++) {
          Cart tmp = list.get(i);
          sum += tmp.getItem().getItemPrice() * tmp.getQuantity();
        }

        return sum;
    }


 /*   @RequestMapping(value = {"/shop/deleteItem/", "shop/deleteItem/{itemId}"})*/
    @PostMapping("shop/index/delete-item/{itemId}")
    public String deleteProduct(@PathVariable("itemId")String itemId,Model model) {
        ModelAndView mv = new ModelAndView("shop/index");

        Item item = itemService.findItemByItemId(Long.parseLong(itemId));
        item.setStatus("NIEAKTYWNA");
        itemService.save(item);
        mv.addObject("categoryList", categoryService.listCategory());
        mv.addObject("itemList", itemService.listItems());
        model.addAttribute("item", new Item());

        return "redirect:/shop/index";
    }



}
