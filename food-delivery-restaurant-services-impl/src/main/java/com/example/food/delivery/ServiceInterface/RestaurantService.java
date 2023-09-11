package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.ApproveRestaurantRequest;
import com.example.food.delivery.Request.RestaurantRequest;
import com.example.food.delivery.Request.UpdateRestaurantRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface RestaurantService {
    ResponseEntity<BaseResponse<?>> addRestaurant(RestaurantRequest restRequest, String restAgentEmail);
    ResponseEntity<BaseResponse<?>> getRestaurants(int page);
    ResponseEntity<BaseResponse<?>> setRestaurantAvailability(String restAgentEmail, String status);
    ResponseEntity<BaseResponse<?>> removeRestaurant(int restId);
    ResponseEntity<BaseResponse<?>> getRestById(int restId);
    ResponseEntity<BaseResponse<?>> approveRestaurant(String restAgentEmail);
    ResponseEntity<BaseResponse<?>> updateRestaurant(UpdateRestaurantRequest updateRestaurant, String restEmail);
    ResponseEntity<BaseResponse<?>> isVerifiedRestaurant(String restAgentEmail);
    ResponseEntity<BaseResponse<?>> getUnVerifiedRestaurants(int page);
    ResponseEntity<BaseResponse<?>> getRestaurantsByIsVeg(int page, boolean isVeg);
}
