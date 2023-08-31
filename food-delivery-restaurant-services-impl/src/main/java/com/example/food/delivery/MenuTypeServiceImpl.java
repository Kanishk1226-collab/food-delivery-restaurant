package com.example.food.delivery;

import com.example.food.delivery.Request.MenuTypeRequest;
import com.example.food.delivery.Request.RestaurantRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.ServiceInterface.MenuTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MenuTypeServiceImpl implements MenuTypeService {
    @Autowired
    private MenuTypeRepository menuTypeRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    public BaseResponse<?> response;

    public synchronized ResponseEntity<BaseResponse<?>> addMenuType(MenuTypeRequest menuTypeRequest, String restAgentEmail) {
        try {
            isVerifiedRestaurant(restAgentEmail);
            MenuType menuType = new MenuType();
            menuType.setMenuTypeName(menuTypeRequest.getMenuTypeName());
            if(menuTypeRepository.existsByMenuTypeNameIgnoreCase(menuTypeRequest.getMenuTypeName())) {
                throw new RestaurantManagementExceptions.DuplicateException("Menu Type already exists");
            }
            menuType.setMenuTypeDesc(menuTypeRequest.getMenuTypeDesc());
            menuTypeRepository.save(menuType);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Menu Type added successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized void isVerifiedRestaurant(String restAgentEmail) {
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

    public synchronized ResponseEntity<?> getAllMenuTypes() {
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, menuTypeRepository.findAll());
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> removeMenuType(int menuTypeId) {
        try {
            MenuType menuType = menuTypeRepository.findById(menuTypeId).orElse(null);
            if (menuType == null) {
                throw new RestaurantManagementExceptions.MenuTypeNotFound("Menu Type not found with ID " + menuTypeId);
            }
            menuTypeRepository.deleteById(menuTypeId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Menu Type removed Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
