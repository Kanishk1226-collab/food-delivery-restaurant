package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.AddRatingRequest;
import com.example.food.delivery.Request.MenuItemRequest;
import com.example.food.delivery.Request.RatingRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface RatingService {
    ResponseEntity<BaseResponse<?>> createRating(RatingRequest ratingRequest);
    ResponseEntity<BaseResponse<?>> addRating(AddRatingRequest addRatingRequest);
    ResponseEntity<BaseResponse<?>> getRating(int menuItemId);
}
