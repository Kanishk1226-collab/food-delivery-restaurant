package com.example.food.delivery;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EntityConstants.MENU_ITEM_TABLE_NAME)
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = EntityConstants.MENU_ITEM_ID)
    private int menuItemId;

    @NotNull(message = "Menu type Id cannot be null")
    @Column(name = EntityConstants.MENU_TYPE_ID)
    private Integer menuTypeId;

    @NotNull(message = "Restaurant Id cannot be null")
    @Column(name = EntityConstants.MENU_RESTAURANT_ID)
    private Integer menuRestId;

    @NotBlank(message = "Menu item name cannot be blank")
    @Column(name = EntityConstants.MENU_ITEM_NAME)
    private String menuItemName;

    @NotBlank(message = "Menu item description cannot be blank")
    @Column(name = EntityConstants.MENU_ITEM_DESCRIPTION)
    private String menuItemDesc;

    @NotNull(message = "Price cannot be null")
    @Column(name = EntityConstants.PRICE)
    private Double price;

    @NotNull(message = "Availability must be provided")
    @Column(name = EntityConstants.MENU_IS_AVAILABLE)
    private boolean isAvailable;

    @NotNull(message = "Is Veg should be provided")
    @Column(name = EntityConstants.MENU_IS_VEG)
    private boolean isVeg;

    @Column(name = EntityConstants.RATING)
    private Double rating;

//    @Column(name = EntityConstants.RATING_COUNT)
//    private Integer ratingCount;
}
