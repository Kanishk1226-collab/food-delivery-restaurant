package com.example.food.delivery;

public class EntityConstants {
    //RESTAURANT TABLE AND COLUMNS
    public static final String RESTAURANT_TABLE_NAME = "restaurant";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String RESTAURANT_NAME = "restaurant_name";
    public static final String REST_AGENT_EMAIL = "rest_agent_email";
    public static final String LOCATION = "location";
    public static final String AVG_RATING = "avg_rating";
    public static final String OPENING_TIME = "opening_time";
    public static final String CLOSING_TIME = "closing_time";
    public static final String REST_STATUS = "status";
    public static final String REST_IS_VEG = "is_veg";
    public static final String REST_IS_VERIFIED = "is_verified";
    public static final String DEL_IS_AVAILABLE = "is_del_available";
    public static final String AVAIL_MSG = "availMsg";

    //MENU-TYPE TABLE AND COLUMNS
    public static final String MENU_TYPE_TABLE_NAME = "menu_type";
    public static final String MENU_TYPE_ID = "menu_type_id";
    public static final String MENU_TYPE_NAME = "menu_type_name";
    public static final String MENU_TYPE_DESCRIPTION = "menu_type_desc";

    //MENU-ITEM TABLE AND COLUMNS
    public static final String MENU_ITEM_TABLE_NAME = "menu_item";
    public static final String MENU_ITEM_ID = "menu_item_id";
    public static final String MENU_RESTAURANT_ID = "restaurant_id";
    public static final String MENU_ITEM_NAME = "menu_item_name";
    public static final String MENU_ITEM_DESCRIPTION = "menu_item_desc";
    public static final String PRICE = "price";
    public static final String MENU_IS_AVAILABLE = "is_available";
    public static final String MENU_IS_VEG = "is_veg";

    //RATING TABLE AND COLUMNDS
    public static final String RATING_TABLE_NAME = "rating";
    public static final String RATING_ID = "rating_id";
    public static final String ORDER_ID = "order_id";
    public static final String RATING = "rating";
    public static final String USER_ID = "user_id";
    public static final String REVIEW = "review";

}