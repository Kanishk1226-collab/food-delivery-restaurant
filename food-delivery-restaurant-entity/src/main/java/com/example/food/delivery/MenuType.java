package com.example.food.delivery;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EntityConstants.MENU_TYPE_TABLE_NAME)
public class MenuType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = EntityConstants.MENU_TYPE_ID)
    private int menuTypeId;

    @NotBlank(message = "Menu type name cannot be blank")
    @Column(name = EntityConstants.MENU_TYPE_NAME)
    private String menuTypeName;

    @NotBlank(message = "Menu type description cannot be blank")
    @Column(name = EntityConstants.MENU_TYPE_DESCRIPTION)
    private String menuTypeDesc;

}
