package com.example.food.delivery.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApproveRestaurantRequest {
    @NotBlank(message = "Admin Email cannot be blank")
    private String adminEmail;

    @NotBlank(message = "Restaurant Agent Email cannot be blank")
    private String restAgentEmail;
}
