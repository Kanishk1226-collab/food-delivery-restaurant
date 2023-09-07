package com.example.food.delivery.Controller;

import com.example.food.delivery.Request.MenuItemFilter;
import com.example.food.delivery.Request.MenuItemRequest;
import com.example.food.delivery.Request.UpdateMenuItemRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.MenuItemServiceImpl;
import com.example.food.delivery.ServiceInterface.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping(value = "/addMenuItem")
    public ResponseEntity<BaseResponse<?>> addMenuItem(@Valid @RequestBody MenuItemRequest menuItemRequest, @RequestParam String restAgentEmail){
        return menuItemService.addMenuItem(menuItemRequest, restAgentEmail);
    }

    @GetMapping(value = "/getMenuItems")
    public ResponseEntity<?> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @PutMapping(value = "/updateMenuItem")
    public ResponseEntity<?> updateMenuItem(@Valid @RequestBody UpdateMenuItemRequest updateMenuItem, String restAgentEmail) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(updateMenuItem, restAgentEmail));
    }


    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<BaseResponse<?>> deleteMenuItem(@PathVariable int menuItemId) {
        return menuItemService.removeMenuItem(menuItemId);
    }

    @GetMapping("/getMenuDetail")
    public ResponseEntity<BaseResponse<?>> getMenuDetail(@RequestParam int menuItemId, int quantity) {
        return menuItemService.getMenuDetail(menuItemId, quantity);
    }

    @GetMapping("/getRestaurantMenu")
    public ResponseEntity<BaseResponse<?>> getRestMenuItems(@RequestParam int restaurantId, int page) {
        return menuItemService.getMenuItemsByRestId(restaurantId, page);
    }

    @GetMapping("/menuItem")
    public ResponseEntity<BaseResponse<?>> getMenuItemByIsVeg(@RequestBody MenuItemFilter menuItemFilter, @RequestParam int page) {
        return menuItemService.getMenuItemByIsVeg(menuItemFilter, page);
    }
}
