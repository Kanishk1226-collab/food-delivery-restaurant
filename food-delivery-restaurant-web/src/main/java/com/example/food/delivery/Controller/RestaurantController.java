package com.example.food.delivery.Controller;

import com.example.food.delivery.Request.ApproveRestaurantRequest;
import com.example.food.delivery.Request.RestaurantRequest;
import com.example.food.delivery.Request.UpdateRestaurantRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.RestaurantServiceImpl;
import com.example.food.delivery.ServiceInterface.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restService;

    @PostMapping(value = "/addRestaurant")
    public ResponseEntity<BaseResponse<?>> addRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest, @RequestParam String restAgentEmail){
        return restService.addRestaurant(restaurantRequest, restAgentEmail);
    }

    @GetMapping(value = "/getRestaurants")
    public ResponseEntity<?> getAllRestaurants(@RequestParam int page) {
        return restService.getAllRestaurants(page);
    }

    @PutMapping("/setRestaurantAvailability")
    public ResponseEntity<BaseResponse<?>> setAvailability(@RequestParam String restAgentEmail, Boolean isAvail) {
        return restService.setRestaurantAvailability(restAgentEmail, isAvail);
    }

    @PutMapping(value = "/verifyRestaurant")
    public ResponseEntity<BaseResponse<?>> verifyRestaurant(@Valid @RequestBody ApproveRestaurantRequest approveRestaurantRequest){
        return restService.approveRestaurant(approveRestaurantRequest);
    }

    @PutMapping(value = "/updateRestaurant")
    public ResponseEntity<BaseResponse<?>> updateRestaurant(@Valid @RequestBody UpdateRestaurantRequest updateRestaurantRequest){
        return restService.updateRestaurant(updateRestaurantRequest);
    }

    @GetMapping(value = "/getRestById")
    public ResponseEntity<BaseResponse<?>> getRestaurantBId(@RequestParam int restId){
        return restService.getRestById(restId);
    }

    @GetMapping(value = "/isVerifiedRestaurant")
    public ResponseEntity<BaseResponse<?>> isVerifiedRestaurant(@RequestParam String restAgentEmail){
        return restService.isVerifiedRestaurant(restAgentEmail);
    }

    @GetMapping(value = "/unverifiedRestaurant")
    public ResponseEntity<BaseResponse<?>> getUnverifiedRestaurant(@RequestParam String adminEmail, int page){
        return restService.getUnVerifiedRestaurants(adminEmail, page);
    }

    @DeleteMapping("/{restId}")
    public ResponseEntity<BaseResponse<?>> deleteRestaurant(@PathVariable int restId) {
        return restService.removeRestaurant(restId);
    }
}
