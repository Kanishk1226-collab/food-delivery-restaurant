package com.example.food.delivery;

import com.example.food.delivery.*;
import com.example.food.delivery.Request.MenuItemRequest;
import com.example.food.delivery.Request.MenuTypeRequest;
import com.example.food.delivery.Request.UpdateMenuItemRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.CartResponse;
import com.example.food.delivery.Response.MenuItemResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.ServiceInterface.MenuItemService;
import com.example.food.delivery.ServiceInterface.MenuTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuTypeRepository menuTypeRepository;

    @Autowired
    public BaseResponse<?> response;

    @Autowired
    private RestTemplate restTemplate;

    public synchronized ResponseEntity<BaseResponse<?>> addMenuItem(MenuItemRequest menuItemRequest, String restAgentEmail) {
        try {
            int restId = isVerifiedRestaurant(restAgentEmail);
            MenuItem menuItem = new MenuItem();
            if(menuItemRepository.existsByMenuRestIdAndMenuItemNameIgnoreCase(restId, menuItemRequest.getMenuItemName())) {
                throw new RestaurantManagementExceptions.DuplicateException("Menu Item named " + menuItemRequest.getMenuItemName() + " already Exists.");
            }
            if(!menuTypeRepository.existsById(menuItemRequest.getMenuTypeId())) {
                throw new RestaurantManagementExceptions.MenuTypeNotFound("Menu Type Not Found");
            }
            menuItem.setMenuTypeId(menuItemRequest.getMenuTypeId());
            menuItem.setMenuRestId(restId);
            menuItem.setMenuItemName(menuItemRequest.getMenuItemName());
            menuItem.setMenuItemDesc(menuItemRequest.getMenuItemDesc());
            menuItem.setPrice(menuItemRequest.getPrice());
            menuItem.setAvailable(menuItemRequest.getIsAvailable());
            menuItem.setVeg(menuItemRequest.getIsVeg());
            menuItemRepository.save(menuItem);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Menu Item added successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<?> getAllMenuItems() {
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, menuItemRepository.findAll());
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> removeMenuItem(int menuItemId) {
        try {
            MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
            if (menuItem == null) {
                throw new RestaurantManagementExceptions.MenuTypeNotFound("Menu Type not found with ID " + menuItemId);
            }
            menuItemRepository.deleteById(menuItemId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Menu Item removed Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> updateMenuItem(UpdateMenuItemRequest updateMenuItem, String restAgentEmail) {
        try {
            int restId = isVerifiedRestaurant(restAgentEmail);
            if(!menuItemRepository.existsById(updateMenuItem.getMenuItemId())) {
                throw new RestaurantManagementExceptions.MenuItemNotFound("No Menu Item Found");
            }
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(updateMenuItem.getMenuItemId());
            MenuItem menuItem = optionalMenuItem.get();
            if(restId != menuItem.getMenuRestId()) {
                throw new RestaurantManagementExceptions.DeniedAccessException("Access Denied");
            }
            if(updateMenuItem.getMenuTypeId() != null) {
                if(!menuTypeRepository.existsById(updateMenuItem.getMenuTypeId())) {
                    throw new RestaurantManagementExceptions.MenuTypeNotFound("Menu Type Not Found");
                }
                menuItem.setMenuTypeId(updateMenuItem.getMenuTypeId());
            }
            if(updateMenuItem.getMenuItemName() != null) {
                if(menuItemRepository.existsByMenuRestIdAndMenuItemNameIgnoreCase(restId, updateMenuItem.getMenuItemName())) {
                    throw new RestaurantManagementExceptions.DuplicateException("Menu Item named " + updateMenuItem.getMenuItemName() + " already Exists.");
                }
                if(updateMenuItem.getMenuItemName().trim().isEmpty()) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Menu Item name should not be empty");
                }
                menuItem.setMenuItemName(updateMenuItem.getMenuItemName());
            }
            if(updateMenuItem.getMenuItemDesc() != null) {
                if(updateMenuItem.getMenuItemDesc().trim().isEmpty()) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Menu Item Description should not be empty");
                }
                menuItem.setMenuItemDesc(updateMenuItem.getMenuItemDesc());
            }
            if(updateMenuItem.getPrice() != null) {
                if(updateMenuItem.getPrice().isNaN() || updateMenuItem.getPrice() <= 0) {
                    throw new RestaurantManagementExceptions.InvalidInputException("Enter Valid Price");
                }
                menuItem.setPrice(updateMenuItem.getPrice());
            }
            if(updateMenuItem.getIsAvailable() != null) {
                menuItem.setAvailable(updateMenuItem.getIsAvailable());
            }
            if(updateMenuItem.getIsVeg() != null) {
                menuItem.setVeg(updateMenuItem.getIsVeg());
            }
            menuItemRepository.save(menuItem);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Menu Item Updated Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getMenuDetail(int menuItemId, int quantity) {
        try {
            Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);
            if(optMenuItem.isEmpty()) {
                throw new RestaurantManagementExceptions.MenuItemNotFound("Menu Item not Found");
            }
            MenuItem menuItem = optMenuItem.get();
            if(!menuItem.isAvailable()) {
                throw new RestaurantManagementExceptions.NonAvailabilityException("Menu Item was not Available");
            }
            Optional<MenuType> optMenuType = menuTypeRepository.findById(menuItem.getMenuTypeId());
            MenuType menuType = optMenuType.get();
            Optional<Restaurant> optRestaurant = restaurantRepository.findById(menuItem.getMenuRestId());
            Restaurant restaurant = optRestaurant.get();
            if(!restaurant.getIsAvailable() || !restaurant.getIsVerified()) {
                throw new RestaurantManagementExceptions.NonAvailabilityException("Restaurant Not Available");
            }
            if(!isCurrentTimeBetweenOpeningAndClosing(restaurant.getOpenTime(), restaurant.getCloseTime())) {
                throw new RestaurantManagementExceptions.NonAvailabilityException("Restaurant will be available at " + restaurant.getOpenTime() + " to " + restaurant.getCloseTime());
            }
            BaseResponse<?> checkDelAgentAvailability = restTemplate.getForObject("http://localhost:8081/user-service/deliveryAgent/delAgentAvailability?restaurantAgentEmail=" + restaurant.getRestAgentEmail(), BaseResponse.class);
            if(!checkDelAgentAvailability.isSuccess()) {
                throw new RestaurantManagementExceptions.NonAvailabilityException(checkDelAgentAvailability.getError());
            }
            CartResponse cartResponse = new CartResponse();
            cartResponse.setRestaurantId(menuItem.getMenuRestId());
            cartResponse.setRestaurantName(restaurant.getRestName());
            cartResponse.setMenuTypeName(menuType.getMenuTypeName());
            cartResponse.setMenuItemName(menuItem.getMenuItemName());
            cartResponse.setTotalPrice(menuItem.getPrice() * quantity);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, cartResponse);
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<BaseResponse<?>> getMenuItemsByRestId(int menuRestId, int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MenuItem> menuItems = menuItemRepository.findByMenuRestId(menuRestId, pageable);
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, menuItems.map(this::getMenuItemResponse).getContent());
        return ResponseEntity.ok(response);
    }

    private MenuItemResponse getMenuItemResponse(MenuItem menuItem) {
        MenuItemResponse menuItemResponse = new MenuItemResponse();
        menuItemResponse.setMenuItemId(menuItem.getMenuItemId());
        menuItemResponse.setMenuRestId(menuItem.getMenuRestId());
        menuItemResponse.setMenuItemName(menuItem.getMenuItemName());
        menuItemResponse.setMenuItemDesc(menuItem.getMenuItemDesc());
        menuItemResponse.setPrice(menuItem.getPrice());
        menuItemResponse.setAvailable(menuItem.isAvailable());
        menuItemResponse.setVeg(menuItem.isVeg());

        MenuType menuType = menuTypeRepository.findById(menuItem.getMenuTypeId()).orElse(null);
        if (menuType != null) {
            menuItemResponse.setMenuTypeName(menuType.getMenuTypeName());
        }
        return menuItemResponse;
    }

    public synchronized int isVerifiedRestaurant(String restAgentEmail) {
        int restId = 0;
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
            restId = restaurant.getRestId();
            return restId;
    }

    public boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
