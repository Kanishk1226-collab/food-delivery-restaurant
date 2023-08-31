package com.example.food.delivery.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuTypeRequest {
    @NotBlank(message = "Menu type name cannot be blank")
    private String menuTypeName;

    @NotBlank(message = "Menu type description cannot be blank")
    private String menuTypeDesc;

}
