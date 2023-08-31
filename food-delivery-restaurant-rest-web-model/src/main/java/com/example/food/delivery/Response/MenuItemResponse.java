package com.example.food.delivery.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {
    private int menuItemId;
    private String menuTypeName;
    private Integer menuRestId;
    private String menuItemName;
    private String menuItemDesc;
    private Double price;
    private boolean isAvailable;
    private boolean isVeg;
}
