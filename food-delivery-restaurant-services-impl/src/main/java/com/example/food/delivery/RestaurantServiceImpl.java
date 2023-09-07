package com.example.food.delivery;

import com.example.food.delivery.Request.ApproveRestaurantRequest;
import com.example.food.delivery.Request.RestaurantRequest;
import com.example.food.delivery.Request.RestaurantStatus;
import com.example.food.delivery.Request.UpdateRestaurantRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.Response.RestaurantResponse;
import com.example.food.delivery.ServiceInterface.RestaurantService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    public BaseResponse<?> response;

    @Autowired
    private RestTemplate restTemplate;

    public synchronized ResponseEntity<BaseResponse<?>> addRestaurant(RestaurantRequest restRequest, String restAgentEmail) {
        try {
//            if(!isValidEmail(restAgentEmail)) {
//                throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Restaurant Agent Email");
//            }
//            BaseResponse<?> isRestAgentValidResponse = restTemplate.getForObject("http://localhost:8081/user-service/restaurantAgent/isRestAgentLoggedIn?restAgentEmail=" + restAgentEmail, BaseResponse.class);
//            if(isRestAgentValidResponse == null) {
//                throw new RestaurantManagementExceptions.RestTemplateException("System Error");
//            }
//            if(!isRestAgentValidResponse.isSuccess()){
//                throw new RestaurantManagementExceptions.RestTemplateException(isRestAgentValidResponse.getError());
//            }
            if (restaurantRepository.existsByRestAgentEmail(restAgentEmail)) {
                throw new RestaurantManagementExceptions.UserAlreadyExistsException("Restaurant with this email already registered");
            }
            String restName = restRequest.getRestName();
            if (restaurantRepository.existsByRestName(restRequest.getRestName())) {
                String isVeg = restRequest.getIsVeg() ? "Pure Veg" : "Non-Veg";
                throw new RestaurantManagementExceptions.DuplicateException("Restaurant name " + restRequest.getRestName() + " already exists. Below are few suggestions to keep " +
                        "to keep your restaurant name unique. \n" +
                        "1. Add Specific location descriptor to differentiate between similar restaurant names \n" +
                        "For example, if \"" + restName + "\" already exists, the you can consider naming restaurant as \"" + restName + " Downtown\" or \"" + restName + " Plaza\" \n" +
                        "2. Highlight Special Features: If your restaurant has unique features, such as offering a specific type of cuisine, incorporate those features into the name. \n" +
                        "For example, \"" + restName + " " + isVeg + "\"");
            }
            Restaurant restaurant = new Restaurant();
            restaurant.setRestName(restRequest.getRestName());
            restaurant.setLocation(restRequest.getLocation());
            restaurant.setAvgRating(null);
            restaurant.setOpenTime(restRequest.getOpenTime());
            restaurant.setCloseTime(restRequest.getCloseTime());
            restaurant.setStatus(RestaurantStatus.NOT_AVAILABLE);
            restaurant.setAvailMsg("Unverified Restaurant");
            restaurant.setVeg(restRequest.getIsVeg());
            restaurant.setRestAgentEmail(restAgentEmail);
            restaurant.setIsVerified(false);
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Restaurant added successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getRestaurants(int page) {
        LocalTime currentTime = LocalTime.now();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Restaurant> restaurants = restaurantRepository.findAllWithCustomSorting(currentTime, pageable);
        List<Restaurant> restaurantList = restaurants.getContent();
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, restaurantList);
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getRestaurantsByIsVeg(int page, boolean isVeg) {
        LocalTime currentTime = LocalTime.now();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Restaurant> restaurants = restaurantRepository.findAllWithIsVegFilter(currentTime, isVeg, pageable);
        List<Restaurant> restaurantList = restaurants.getContent();
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, restaurantList);
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> setRestaurantAvailability(String restAgentEmail, String status) {
        try {
            if(!isValidEmail(restAgentEmail)) {
                throw new RestaurantManagementExceptions.InvalidInputException("Enter valid Restaurant Agent Email");
            }
            Restaurant restaurant = restaurantRepository.findByRestAgentEmail(restAgentEmail);
            if(restaurant == null) {
                throw new RestaurantManagementExceptions.RestaurantNotFound("Restaurant Not Found with email " + restAgentEmail);
            }
            if(!restaurant.getIsVerified()) {
                throw new RestaurantManagementExceptions.VerifyException("Restaurant Not Verified");
            }
            if(!EnumUtils.isValidEnum(RestaurantStatus.class, status.toUpperCase())) {
                throw new RestaurantManagementExceptions.InvalidInputException("Status should be either AVAILABLE or NOT_AVAILABLE");
            }
            restaurant.setStatus(RestaurantStatus.valueOf(status.toUpperCase()));
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Availability status changed");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> removeRestaurant(int restId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restId).orElse(null);
            if (restaurant == null) {
                throw new RestaurantManagementExceptions.RestaurantNotFound("Restaurant not found with ID " + restId);
            }
            restaurantRepository.deleteById(restId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "User removed Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getRestById(int restId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restId).orElse(null);
            if (restaurant == null) {
                throw new RestaurantManagementExceptions.RestaurantNotFound("Restaurant not found with ID " + restId);
            }
            RestaurantResponse restResponse = RestaurantResponse.builder()
                    .restName(restaurant.getRestName())
                    .restAgentEmail(restaurant.getRestAgentEmail())
                    .location(restaurant.getLocation())
                    .avgRating(restaurant.getAvgRating())
                    .isVeg(restaurant.isVeg())
                    .build();
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, restResponse);
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> approveRestaurant(ApproveRestaurantRequest approveRestaurantRequest) {
        try {
            String adminEmail = approveRestaurantRequest.getAdminEmail();
            String restAgentEmail = approveRestaurantRequest.getRestAgentEmail();
            if(!isValidEmail(adminEmail)) {
                throw new RestaurantManagementExceptions.InvalidInputException("Enter valid Admin Email");
            }
            isValidAdminEmail(adminEmail);
            if(!isValidEmail(restAgentEmail)) {
                throw new RestaurantManagementExceptions.InvalidInputException("Enter valid Restaurant Agent Email");
            }
            Restaurant restaurant = restaurantRepository.findByRestAgentEmail(restAgentEmail);
            if(restaurant == null) {
                throw new RestaurantManagementExceptions.RestaurantNotFound("No Restaurant found for id " + restAgentEmail);
            }
            if(restaurant.getIsVerified()) {
                throw new RestaurantManagementExceptions.VerifyException("Restaurant already verified");
            }
            restaurant.setIsVerified(true);
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Restaurant verified Successfully");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> updateRestaurant(UpdateRestaurantRequest updateRestaurant) {
        try {
            String restEmail = updateRestaurant.getRestAgentEmail();
            verifyRestaurant(restEmail);
            Restaurant restaurant = restaurantRepository.findByRestAgentEmail(restEmail);
            if(updateRestaurant.getOpenTime() != null) {
                if(!isValidTime(updateRestaurant.getOpenTime())) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Opening Time | Format should be HH:mm");
                }
                restaurant.setOpenTime(updateRestaurant.getOpenTime());
            }
            if(updateRestaurant.getCloseTime() != null) {
                if(!isValidTime(updateRestaurant.getCloseTime())) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Closing Time | Format should be HH:mm");
                }
                restaurant.setCloseTime(updateRestaurant.getCloseTime());
            }
            if(updateRestaurant.getIsAvailable() != null) {
                restaurant.setStatus(updateRestaurant.getIsAvailable());
            }
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Restaurant updated Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized void verifyRestaurant(String restAgentEmail) {
        if(!isValidEmail(restAgentEmail)) {
            throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Restaurant Agent Email");
        }
        Restaurant restaurant = restaurantRepository.findByRestAgentEmail(restAgentEmail);
        if(restaurant == null) {
            throw new RestaurantManagementExceptions.RestaurantNotFound("No Restaurant registered on " + restAgentEmail);
        }
        if(!restaurant.getIsVerified()) {
            throw new RestaurantManagementExceptions.VerifyException("Restaurant not verified");
        }
    }

    public synchronized ResponseEntity<BaseResponse<?>> isVerifiedRestaurant(String restAgentEmail) {
        try {
            verifyRestaurant(restAgentEmail);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Verified Restaurant");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getUnVerifiedRestaurants(String adminEmail, int page) {
        try {
            if(!isValidEmail(adminEmail)) {
                throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Admin Email");
            }
            isValidAdminEmail(adminEmail);
            int pageSize = 10;
            Sort sortById = Sort.by(Sort.Direction.ASC, "restId");
            PageRequest pageRequest = PageRequest.of(page, pageSize, sortById);
            List<Restaurant> restaurant = restaurantRepository.findByIsVerifiedFalse(pageRequest);
            if(restaurant == null) {
                throw new RestaurantManagementExceptions.RestaurantNotFound("No Restaurants found Unverified");
            }
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, restaurant);
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public void isValidAdminEmail(String email) {
        BaseResponse<?> isAdminValidResponse = restTemplate.getForObject("http://localhost:8081/user-service/admins/isAdminLoggedIn?adminEmail=" + email, BaseResponse.class);
        if(isAdminValidResponse == null) {
            throw new RestaurantManagementExceptions.RestTemplateException("System Error");
        }
        if(!isAdminValidResponse.isSuccess()){
            throw new RestaurantManagementExceptions.RestTemplateException(isAdminValidResponse.getError());
        }
    }

    public boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidTime(String time)
    {
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern p = Pattern.compile(regex);
        if (time == null) {
            return false;
        }
        Matcher m = p.matcher(time);
        return m.matches();
    }

}
