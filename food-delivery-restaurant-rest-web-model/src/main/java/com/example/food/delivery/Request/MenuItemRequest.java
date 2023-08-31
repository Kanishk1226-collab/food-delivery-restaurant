package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    @NotNull(message = "Menu type Id cannot be null")
    @Positive(message = "Enter Valid Menu Type Id")
    private Integer menuTypeId;

//    @NotNull(message = "Restaurant Id cannot be null")
//    private Integer menuRestId;

    @NotBlank(message = "Menu item name cannot be blank")
    private String menuItemName;

    @NotBlank(message = "Menu item description cannot be blank")
    private String menuItemDesc;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Enter Valid Price Value")
    private Double price;

    @NotNull(message = "Availability must be provided")
    @ValidBooleanValue(message = "Invalid availability value")
    private Boolean isAvailable;

    @NotNull(message = "Is Veg should be provided")
    @ValidBooleanValue(message = "Invalid Is Veg value")
    private Boolean isVeg;
}
