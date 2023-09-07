package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.MenuItemFilter;
import com.example.food.delivery.Request.MenuItemRequest;
import com.example.food.delivery.Request.UpdateMenuItemRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface MenuItemService {
    ResponseEntity<BaseResponse<?>> addMenuItem(MenuItemRequest menuItemRequest, String restAgentEmail);
    ResponseEntity<?> getAllMenuItems();
    ResponseEntity<BaseResponse<?>> removeMenuItem(int menuItemId);
    ResponseEntity<BaseResponse<?>> updateMenuItem(UpdateMenuItemRequest updateMenuItem, String restAgentEmail);
    ResponseEntity<BaseResponse<?>> getMenuDetail(int menuItemId, int quantity);
    ResponseEntity<BaseResponse<?>> getMenuItemsByRestId(int menuRestId, int page);
    ResponseEntity<BaseResponse<?>> getMenuItemByIsVeg(MenuItemFilter menuItemFilter, int page);

}
