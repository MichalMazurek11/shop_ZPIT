package com.project.shop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;


    @NotBlank(message = "Musisz wypełnic nazwe produktu")
    private String itemName;

 //   @NotBlank(message = "Musisz wypelnic pole")
    private String itemDescription;


    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category = new Category();


    private String image;

    private Float itemPrice;

    private int quantity = 1;

    private String status = "AKTYWNA";

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private List<Cart> carts;


    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", category=" + category +
                ", image='" + image + '\'' +
                ", itemPrice=" + itemPrice +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", carts=" + carts +
                '}';
    }
}
