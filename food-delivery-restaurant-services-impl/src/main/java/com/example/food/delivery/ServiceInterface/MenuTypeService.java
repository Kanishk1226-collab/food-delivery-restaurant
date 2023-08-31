package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.MenuItemRequest;
import com.example.food.delivery.Request.MenuTypeRequest;
import com.example.food.delivery.Request.UpdateMenuItemRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface MenuTypeService {
    ResponseEntity<BaseResponse<?>> addMenuType(MenuTypeRequest menuTypeRequest, String restAgentEmail);
    ResponseEntity<?> getAllMenuTypes();
    ResponseEntity<BaseResponse<?>> removeMenuType(int menuTypeId);

}
