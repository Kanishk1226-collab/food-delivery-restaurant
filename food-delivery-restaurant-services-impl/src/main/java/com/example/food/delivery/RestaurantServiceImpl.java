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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
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
        updateRestaurantStatusAndMessage();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Restaurant> restaurants = restaurantRepository.findAllWithCustomSorting(pageable);
        List<Restaurant> restaurantList = restaurants.getContent();
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, restaurantList);
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getRestaurantsByIsVeg(int page, boolean isVeg) {
        updateRestaurantStatusAndMessage();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Restaurant> restaurants = restaurantRepository.findAllWithIsVegFilter(isVeg, pageable);
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
                throw new RestaurantManagementExceptions.InvalidInputException("Status should be either AVAILABLE or NOT_DELIVERABLE");
            }
            String restStatus = status.toUpperCase();
            if(restaurant.getStatus() != RestaurantStatus.NOT_AVAILABLE && restaurant.getStatus() != RestaurantStatus.NOT_OPEN) {
                if(restaurant.getStatus() == RestaurantStatus.NOT_DELIVERABLE && restStatus.equals("AVAILABLE")) {
                    if((!isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime()))) {
                        restaurant.setStatus(RestaurantStatus.NOT_OPEN);
                        restaurant.setAvailMsg("Restaurant will be open from " + restaurant.getOpenTime() + " hrs to " + restaurant.getCloseTime());
                    } else {
                        restaurant.setStatus(RestaurantStatus.AVAILABLE);
                        restaurant.setAvailMsg("Restaurant currently available to deliver foods.");
                    }
                } else if(restaurant.getStatus() == RestaurantStatus.AVAILABLE && restStatus.equals("NOT_DELIVERABLE")) {
                    restaurant.setStatus(RestaurantStatus.NOT_DELIVERABLE);
                    restaurant.setAvailMsg("Not Delivering currently.");
                }
            }
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

    public synchronized ResponseEntity<BaseResponse<?>> approveRestaurant(String restAgentEmail) {
        try {
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
            restaurant.setAvailMsg("Restaurant Verified");
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Restaurant verified Successfully");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> updateRestaurant(UpdateRestaurantRequest updateRestaurant, String restEmail) {
        try {
            verifyRestaurant(restEmail);
            Restaurant restaurant = restaurantRepository.findByRestAgentEmail(restEmail);
            String responseMessage = "Restaurant detail updated successfully.";
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
            if(updateRestaurant.getStatus() != null) {
                String status = updateRestaurant.getStatus().toUpperCase();
                if(!EnumUtils.isValidEnum(RestaurantStatus.class, status)) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Status should be either AVAILABLE or NOT_AVAILABLE");
                }
                if(status.equals("AVAILABLE")) {
                    if((!isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime()))) {
                        throw new RestaurantManagementExceptions.NonAvailabilityException("You cannot change your restaurant status to AVAILABLE before opening time or " +
                                "after closing time. You can able to change status only when restaurant is opened to deliver orders.");
                    }
                    BaseResponse<?> checkDelAgentAvailability = restTemplate.getForObject("http://localhost:8081/user-service/delAgent/checkStatus?restaurantAgentEmail=" + restaurant.getRestAgentEmail(), BaseResponse.class);
                    if(!checkDelAgentAvailability.isSuccess()) {
                        restaurant.setStatus(RestaurantStatus.NOT_DELIVERABLE);
                        restaurant.setAvailMsg("Not Delivering currently. You can add items to cart and checkout after sometime.");
                        responseMessage = responseMessage + "Since no delivery partners are currently available for you restaurant to deliver food, the status " +
                                "changed to NOT DELIVERABLE. Once delivery partners are available, status will be automatically changed to AVAILABLE.";
                    } else if((!isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime()))) {
                        restaurant.setStatus(RestaurantStatus.NOT_OPEN);
                        restaurant.setAvailMsg("Restaurant will be open from " + restaurant.getOpenTime() + " hrs to " + restaurant.getCloseTime());
                        responseMessage = responseMessage + "Since restaurant will be available from " + restaurant.getOpenTime() + " hrs to " + restaurant.getCloseTime() +
                                ", the status will changed to NOT_OPEN. ";
                    } else {
                        restaurant.setStatus(RestaurantStatus.AVAILABLE);
                        restaurant.setAvailMsg("Restaurant currently available to deliver foods.");
                        }
                } else if(status.equals("NOT_AVAILABLE")) {
                    restaurant.setStatus(RestaurantStatus.valueOf(status));
                    restaurant.setAvailMsg("Currently Restaurant was not available to deliver foods.");
                    responseMessage = responseMessage + "Note: Changing the status of restaurant to NOT AVAILABLE, it is Restaurant Agent's responsibility " +
                            "to change the status back to AVAILABLE. You can change the status back to AVAILABLE only when Restaurant is opened to deliver foods.";
                } else {
                    throw new RestaurantManagementExceptions.InvalidInputException("Status should be either AVAILABLE or NOT_AVAILABLE");
                }
            }
            restaurantRepository.save(restaurant);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, responseMessage);
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

    public synchronized ResponseEntity<BaseResponse<?>> getUnVerifiedRestaurants(int page) {
        try {
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

    public void updateRestaurantStatusAndMessage() {
        List<Restaurant> restaurants = restaurantRepository.findByStatus(RestaurantStatus.AVAILABLE, RestaurantStatus.NOT_OPEN);
        for (Restaurant restaurant : restaurants) {
            RestaurantStatus currentStatus = restaurant.getStatus();
            if (currentStatus == RestaurantStatus.AVAILABLE) {
                if ((!isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime()))) {
                    restaurant.setStatus(RestaurantStatus.NOT_OPEN);
                    restaurant.setAvailMsg("Restaurant will be open from " + restaurant.getOpenTime() + " hrs to " + restaurant.getCloseTime() + " hrs.");
                }
            } else if (currentStatus == RestaurantStatus.NOT_OPEN) {
                if ((isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime()))) {
                    BaseResponse<?> checkDelAgentAvailability = restTemplate.getForObject("http://localhost:8081/user-service/delAgent/checkStatus?restaurantAgentEmail=" + restaurant.getRestAgentEmail(), BaseResponse.class);
                    if (!checkDelAgentAvailability.isSuccess()) {
                        restaurant.setStatus(RestaurantStatus.NOT_DELIVERABLE);
                        restaurant.setAvailMsg("Not Delivering currently. You can add items to cart and checkout after sometime.");
                    } else {
                        restaurant.setStatus(RestaurantStatus.AVAILABLE);
                        restaurant.setAvailMsg("Restaurant currently available to deliver foods.");
                    }
                }
            }
            restaurantRepository.save(restaurant);
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

    public boolean isCurrentTimeBetweenOpeningAndClosing(String openingTime, String closingTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date time1 = sdf.parse(openingTime);
            Date time2 = sdf.parse(closingTime);
            Calendar currentTime = Calendar.getInstance();
            Date currentTimeValue = currentTime.getTime();
            String currentTimeStr = sdf.format(currentTimeValue);
            Date currentTime24Value = sdf.parse(currentTimeStr);
            return currentTime24Value.after(time1) && currentTime24Value.before(time2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
