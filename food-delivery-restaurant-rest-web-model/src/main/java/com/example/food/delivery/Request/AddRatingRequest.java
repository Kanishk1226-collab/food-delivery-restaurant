package com.example.food.delivery.Request;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRatingRequest {
    @NotNull(message = "Order Id cannot be null")
    private Integer orderId;

    @NotBlank(message = "Customer Email Id cannot be null")
    @Email(message = "Enter Valid Email Id")
    private String customerEmail;

    @NotNull(message = "Menu Item Id cannot be null")
    private Integer menuItemId;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 0, message = "Minimum should be zero")
    @Max(value = 5, message = "Maximum should be five")
    private Integer rating;
}
