package com.example.food.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT r FROM Rating r WHERE r.orderId = :orderId AND r.menuItemId = :menuItemId")
    Rating findByOrderIdAndMenuItemId(Integer orderId, Integer menuItemId);
    List<Rating> findByMenuItemId(Integer menuItemId);
    List<Rating> findByMenuItemIdOrderByRatingIdDesc(Integer menuItemId);

}
