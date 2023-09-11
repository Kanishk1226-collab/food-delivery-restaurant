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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping(value = "/add")
    @PreAuthorize("#userRole == 'RESTAURANT_AGENT'")
    public ResponseEntity<BaseResponse<?>> addMenuItem(@Valid @RequestBody MenuItemRequest menuItemRequest,
                                                       @RequestHeader("userEmail") String userEmail,
                                                       @RequestHeader("userRole") String userRole){
        return menuItemService.addMenuItem(menuItemRequest, userEmail);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("#userRole == 'ADMIN'")
    public ResponseEntity<?> getAllMenuItems(@RequestHeader("userEmail") String userEmail,
                                             @RequestHeader("userRole") String userRole) {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @PutMapping(value = "/update")
    @PreAuthorize("#userRole == 'RESTAURANT_AGENT'")
    public ResponseEntity<?> updateMenuItem(@Valid @RequestBody UpdateMenuItemRequest updateMenuItem,
                                            @RequestHeader("userEmail") String userEmail,
                                            @RequestHeader("userRole") String userRole) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(updateMenuItem, userEmail));
    }


    @DeleteMapping("/delete/{menuItemId}")
    @PreAuthorize("#userRole == 'RESTAURANT_AGENT'")
    public ResponseEntity<BaseResponse<?>> deleteMenuItem(@PathVariable int menuItemId,
                                                          @RequestHeader("userEmail") String userEmail,
                                                          @RequestHeader("userRole") String userRole) {
        return menuItemService.removeMenuItem(menuItemId);
    }

    @GetMapping("/menuDetail")
    public ResponseEntity<BaseResponse<?>> getMenuDetail(@RequestParam int menuItemId, int quantity) {
        return menuItemService.getMenuDetail(menuItemId, quantity);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<BaseResponse<?>> getRestMenuItems(@RequestParam int restaurantId, int page) {
        return menuItemService.getMenuItemsByRestId(restaurantId, page);
    }

    @GetMapping("/isVeg")
    public ResponseEntity<BaseResponse<?>> getMenuItemByIsVeg(@RequestBody MenuItemFilter menuItemFilter, @RequestParam int page) {
        return menuItemService.getMenuItemByIsVeg(menuItemFilter, page);
    }
}
