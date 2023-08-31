package com.example.food.delivery.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BaseResponse<T> {
    private boolean success;
    private String status;
    private String error;
    private T data;

    public static <T> BaseResponse<T> createSystemErrorResponse() {
        return new BaseResponse<>(false, "error", "System Error", null);
    }
}
