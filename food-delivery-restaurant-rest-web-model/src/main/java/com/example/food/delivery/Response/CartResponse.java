package com.example.food.delivery.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private int restaurantId;
    private String restaurantName;
    private String menuTypeName;
    private String menuItemName;
    private double totalPrice;

}
