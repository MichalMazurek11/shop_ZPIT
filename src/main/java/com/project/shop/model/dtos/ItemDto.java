package com.project.shop.model.dtos;


import com.project.shop.model.Category;
import com.project.shop.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ItemDto {

    @NotBlank(message = "Musisz wypełnic nazwe produktu")
    private String itemName;

    private String itemDescription;

    private Category category = new Category();

    private String image;

    private Float itemPrice;

    private String status = "AKTYWNA";

    private User user;





}
