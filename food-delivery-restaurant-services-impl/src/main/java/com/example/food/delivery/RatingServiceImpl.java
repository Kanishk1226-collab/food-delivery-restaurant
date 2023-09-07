package com.example.food.delivery;

import com.example.food.delivery.Request.AddRatingRequest;
import com.example.food.delivery.Request.RatingRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.RatingResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.ServiceInterface.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    public RatingRepository ratingRepository;

    @Autowired
    public BaseResponse<?> response;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public ResponseEntity<BaseResponse<?>> createRating(RatingRequest ratingRequest) {
        try {
            int menuId = ratingRequest.getMenuItemId();
            if(!menuItemRepository.existsById(menuId)) {
                throw new RestaurantManagementExceptions.MenuItemNotFound("Menu Item with ID " + menuId + " not found.");
            }
            Rating rating = Rating.builder()
                    .menuItemId(menuId)
                    .customerEmail(ratingRequest.getCustomerEmail())
                    .orderId(ratingRequest.getOrderId())
                    .rating(null)
                    .review("")
                    .build();
            ratingRepository.save(rating);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Rating created successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> addRating(AddRatingRequest addRatingRequest) {
        try {
            int orderId = addRatingRequest.getOrderId();
            int menuId = addRatingRequest.getMenuItemId();
            Rating rating = ratingRepository.findByOrderIdAndMenuItemId(orderId, menuId);
            if(rating == null) {
                throw new RestaurantManagementExceptions.MenuItemNotFound("No order found");
            }
            if(!rating.getCustomerEmail().equalsIgnoreCase(addRatingRequest.getCustomerEmail())) {
                throw new RestaurantManagementExceptions.DeniedAccessException("Un-Authorized Access");
            }
            if(rating.getRating() != null) {
                throw new RestaurantManagementExceptions.DuplicateException("There is already Rating given for this order.");
            }
            rating = Rating.builder()
                    .rating(addRatingRequest.getRating())
                    .build();
            ratingRepository.save(rating);
            updateAvgRatingInMenu(menuId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Rating updated successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public void updateAvgRatingInMenu(int menuItemId) {
        Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);
        MenuItem menuItem = optMenuItem.get();
        List<Rating> ratings = ratingRepository.findByMenuItemId(menuItemId);
        List<Rating> validRatings = ratings.stream()
                .filter(rating -> rating.getRating() != null)
                .collect(Collectors.toList());

        int totalRating = 0;
        int count = validRatings.size();
        for (Rating rating : validRatings) {
            totalRating += rating.getRating();
        }
        double avgRating = totalRating / count;
        menuItem.setRating(avgRating);
        menuItemRepository.save(menuItem);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> getRating(int menuItemId) {
        try {
            if(!menuItemRepository.existsById(menuItemId)) {
                throw new RestaurantManagementExceptions.MenuItemNotFound("Menu Item with ID " + menuItemId + " not found.");
            }
            List<Rating> ratings = ratingRepository.findByMenuItemIdOrderByRatingIdDesc(menuItemId);
            List<Rating> validRatings = ratings.stream()
                    .filter(rating -> rating.getRating() != null)
                    .collect(Collectors.toList());
            List<RatingResponse> ratingsList = new ArrayList<>();
            for(Rating rating : validRatings) {
                String menuName = menuItemRepository.findMenuItemNameByMenuItemId(menuItemId);
                RatingResponse ratingResponse = new RatingResponse();
                ratingResponse.setCustomerEmail(rating.getCustomerEmail());
                ratingResponse.setMenuItemName(menuName);
                ratingResponse.setRating(rating.getRating());
                ratingResponse.setReview(rating.getReview().trim().isEmpty() ? "No Review" : rating.getReview());
                ratingsList.add(ratingResponse);
            }
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, ratingsList);
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }
}
