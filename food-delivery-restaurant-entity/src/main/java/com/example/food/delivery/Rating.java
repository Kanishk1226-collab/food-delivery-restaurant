package com.example.food.delivery;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EntityConstants.RATING_TABLE_NAME)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = EntityConstants.RATING_ID)
    private int ratingId;

    @NotNull(message = "Order Id cannot be null")
    @Column(name = EntityConstants.ORDER_ID)
    private Integer orderId;

    @NotBlank(message = "Customer Email Id cannot be null")
    @Email(message = "Enter Valid Email Id")
    @Column(name = EntityConstants.USER_ID)
    private String customerEmail;

    @NotNull(message = "Menu Item Id cannot be null")
    @Column(name = EntityConstants.MENU_ITEM_ID)
    private Integer menuItemId;

    @Column(name = EntityConstants.RATING)
    private Integer rating;

    @Column(name = EntityConstants.REVIEW)
    private String review;
}
