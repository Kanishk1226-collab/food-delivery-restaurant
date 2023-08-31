package com.example.food.delivery.Controller;

import com.example.food.delivery.MenuTypeServiceImpl;
import com.example.food.delivery.Request.MenuTypeRequest;
import com.example.food.delivery.Request.RestaurantRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.RestaurantServiceImpl;
import com.example.food.delivery.ServiceInterface.MenuTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuType")
public class MenuTypeController {
    @Autowired
    private MenuTypeService menuTypeService;

    @PostMapping(value = "/addMenuType")
    public ResponseEntity<BaseResponse<?>> addMenuType(@Valid @RequestBody MenuTypeRequest menuTypeRequest, @RequestParam String restAgentEmail){
        return menuTypeService.addMenuType(menuTypeRequest, restAgentEmail);
    }

    @GetMapping(value = "/getMenuTypes")
    public ResponseEntity<?> getAllMenuTypes() {
        return ResponseEntity.ok(menuTypeService.getAllMenuTypes());
    }

    @DeleteMapping("/{menuTypeId}")
    public ResponseEntity<BaseResponse<?>> deleteMenuType(@PathVariable int menuTypeId) {
        return menuTypeService.removeMenuType(menuTypeId);
    }
}
