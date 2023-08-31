package com.example.food.delivery.Request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantRequest {
    @Email(message = "Enter valid Restaurant Agent Email")
    private String restAgentEmail;
    private String openTime;
    private String closeTime;
    private Boolean isAvailable;
}
