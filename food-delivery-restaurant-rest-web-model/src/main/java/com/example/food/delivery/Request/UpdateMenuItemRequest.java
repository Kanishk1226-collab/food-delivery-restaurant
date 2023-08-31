package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuItemRequest {
    @Positive(message = "Menu Item ID should be greater than zero")
    @NotNull(message = "Menu Item Field should not be null")
    private Integer menuItemId;
    private Integer menuTypeId;
    private String menuItemName;
    private String menuItemDesc;
    private Double price;
    private Boolean isAvailable;
    private Boolean isVeg;
}
