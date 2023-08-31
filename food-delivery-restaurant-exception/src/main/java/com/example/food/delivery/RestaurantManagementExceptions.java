package com.example.food.delivery;

public class RestaurantManagementExceptions {
    public static class BaseRestaurantManagementException extends RuntimeException {
        private boolean isSuccess;
        private String status;

        public BaseRestaurantManagementException(String message) {
            super(message);
            this.isSuccess = false;
            this.status = "Error";
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class InvalidInputException extends BaseRestaurantManagementException {
        public InvalidInputException(String message) {
            super(message);
        }
    }


    public static class UnrecognizedTokenException extends RuntimeException {
        public UnrecognizedTokenException(String message) {
            super(message);
        }
    }


    public static class RestaurantNotFound extends BaseRestaurantManagementException {
        public RestaurantNotFound(String message) {
            super(message);
        }
    }

    public static class RestTemplateException extends BaseRestaurantManagementException {
        public RestTemplateException(String message) {
            super(message);
        }
    }

    public static class MenuTypeNotFound extends BaseRestaurantManagementException {
        public MenuTypeNotFound(String message) {
            super(message);
        }
    }

    public static class DeniedAccessException extends BaseRestaurantManagementException {
        public DeniedAccessException(String message) {
            super(message);
        }
    }

    public static class DuplicateException extends BaseRestaurantManagementException {
        public DuplicateException(String message) {
            super(message);
        }
    }


    public static class MenuItemNotFound extends BaseRestaurantManagementException {
        public MenuItemNotFound(String message) {
            super(message);
        }
    }

    public static class NonAvailabilityException extends BaseRestaurantManagementException {
        public NonAvailabilityException(String message) {
            super(message);
        }
    }

    public static class VerifyException extends BaseRestaurantManagementException {
        public VerifyException(String message) {
            super(message);
        }
    }


    public static class UserAlreadyExistsException extends BaseRestaurantManagementException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }


}