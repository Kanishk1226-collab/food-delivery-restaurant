package com.example.food.delivery.Controller;

import com.example.food.delivery.Request.AddRatingRequest;
import com.example.food.delivery.Request.MenuTypeRequest;
import com.example.food.delivery.Request.RatingRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.MenuTypeService;
import com.example.food.delivery.ServiceInterface.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @PostMapping(value = "/create")
    public ResponseEntity<BaseResponse<?>> createRating(@Valid @RequestBody RatingRequest ratingRequest){
        return ratingService.createRating(ratingRequest);
    }

    @PutMapping(value = "/addRating")
    public ResponseEntity<BaseResponse<?>> addRating(@Valid @RequestBody AddRatingRequest addRatingRequest){
        return ratingService.addRating(addRatingRequest);
    }

    @PutMapping(value = "/ratingById")
    public ResponseEntity<BaseResponse<?>> getRating(int menuItemId){
        return ratingService.getRating(menuItemId);
    }

}
