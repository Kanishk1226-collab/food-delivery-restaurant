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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restService;

    @PostMapping(value = "/restaurant/add")
    @PreAuthorize("#userRole == 'RESTAURANT_AGENT'")
    public ResponseEntity<BaseResponse<?>> addRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest,
                                                         @RequestHeader("userEmail") String userEmail,
                                                         @RequestHeader("userRole") String userRole){
        return restService.addRestaurant(restaurantRequest, userEmail);
    }

    @GetMapping(value = "/all/restaurants")
    @PreAuthorize("#userRole == 'ADMIN'")
    public ResponseEntity<?> getRestaurants(@RequestParam int page,
                                            @RequestHeader("userEmail") String userEmail,
                                            @RequestHeader("userRole") String userRole) {
        System.out.println(userEmail);
        return restService.getRestaurants(page);
    }

    @GetMapping(value = "/restaurants")
    public ResponseEntity<?> getRestaurantsByIsVeg(@RequestParam boolean isVeg, @RequestParam int page) {
        return restService.getRestaurantsByIsVeg(page, isVeg);
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
