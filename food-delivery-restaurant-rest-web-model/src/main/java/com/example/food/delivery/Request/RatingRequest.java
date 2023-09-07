package com.example.food.delivery.Request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @NotNull(message = "Order Id cannot be null")
    private Integer orderId;

    @NotNull(message = "Menu Item Id cannot be null")
    private Integer menuItemId;

    @NotBlank(message = "Customer Email Id cannot be null")
    @Email(message = "Enter Valid Email Id")
    private String customerEmail;
}