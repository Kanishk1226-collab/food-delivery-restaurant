package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MenuItemFilter {
    @NotNull(message = "Restaurant Id cannot be null")
    @Positive(message = "Enter Valid Restaurant Id")
    private Integer menuRestId;

    @NotNull(message = "Is Veg should be provided")
    @ValidBooleanValue(message = "Invalid Is Veg value")
    private Boolean isVeg;
}
