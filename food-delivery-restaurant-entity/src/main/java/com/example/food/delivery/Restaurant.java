package com.example.food.delivery;

import com.example.food.delivery.Request.RestaurantStatus;
import com.example.food.delivery.Validator.EnumNamePattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EntityConstants.RESTAURANT_TABLE_NAME)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = EntityConstants.RESTAURANT_ID)
    private int restId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Column(name = EntityConstants.RESTAURANT_NAME)
    private String restName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Restaurant Agent email cannot be blank")
    @Column(name = EntityConstants.REST_AGENT_EMAIL)
    private String restAgentEmail;

    @NotBlank(message = "Location cannot be blank")
    @Column(name = EntityConstants.LOCATION)
    private String location;

//    @Min(value = -1, message = "Rating must be at least 1")
//    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = EntityConstants.AVG_RATING)
    private Integer avgRating;

    @NotNull(message = "Opening Time cannot be null")
    @Column(name = EntityConstants.OPENING_TIME)
    private String openTime;

    @NotNull(message = "Closing Time cannot be null")
    @Column(name = EntityConstants.CLOSING_TIME)
    private String closeTime;

//    @EnumNamePattern(
//            regexp = "AVAILABLE|NOT_AVAILABLE|NOT_DELIVERABLE|NOT_OPEN",
//            message = "Role Should be either ADMIN or CO_ADMIN"
//    )
    @Column(name = EntityConstants.REST_STATUS)
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @NotBlank(message = "Availability message cannot be blank")
    @Column(name = EntityConstants.AVAIL_MSG)
    private String availMsg;

    @NotNull(message = "Is Veg must be provided")
    @Column(name = EntityConstants.REST_IS_VEG)
    private boolean isVeg;

    @Column(name = EntityConstants.REST_IS_VERIFIED)
    private Boolean isVerified;

}
