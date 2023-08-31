package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import com.example.food.delivery.Validator.ValidTimeFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class RestaurantRequest {
    @NotBlank(message = "Restaurant name cannot be blank")
    private String restName;

    @NotBlank(message = "Location cannot be blank")
    private String location;

//    @Min(value = 0, message = "Rating must be at least 1")
//    @Max(value = 5, message = "Rating must be at most 5")
//    private int avgRating;

    @NotNull(message = "Opening Time cannot be null")
    @ValidTimeFormat
    private String openTime;

    @NotNull(message = "Closing Time cannot be null")
    @ValidTimeFormat
    private String closeTime;

//    @NotNull(message = "Availability must be provided")
//    @ValidBooleanValue(message = "Invalid availability value")
//    private Boolean isAvailable;

    @NotNull(message = "Is Veg must be provided")
    @ValidBooleanValue(message = "Invalid Is Veg value")
    private Boolean isVeg;

}
