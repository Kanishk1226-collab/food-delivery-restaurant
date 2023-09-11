package com.example.food.delivery;

import com.example.food.delivery.Request.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    boolean existsByRestAgentEmail(String restAgentEmail);
    Restaurant findByRestAgentEmail(String restAgentEmail);
    boolean existsByRestName(String restName);

    @Override
    Optional<Restaurant> findById(Integer integer);

    List<Restaurant> findByIsVerifiedFalse(PageRequest pageRequest);

    @Query("SELECT r FROM Restaurant r WHERE r.status = :status1 OR r.status = :status2")
    List<Restaurant> findByStatus(@Param("status1") RestaurantStatus status1, @Param("status2") RestaurantStatus status2);

//    @Query("SELECT r FROM Restaurant r " +
//            "ORDER BY " +
//            "CASE WHEN r.isAvailable = false THEN 1 ELSE 0 END, " +
//            "CASE WHEN r.isVerified = false THEN 2 ELSE 0 END, " +
//            "CASE WHEN ?1 NOT BETWEEN r.openTime AND r.closeTime THEN 3 ELSE 0 END")
//    Page<Restaurant> findAllWithCustomSorting(LocalTime currentTime, Pageable pageable);

//    @Query("SELECT r FROM Restaurant r " +
//            "WHERE r.isVerified = true " +
//            "ORDER BY " +
//            "CASE WHEN r.isAvailable = false THEN 1 ELSE 0 END, " +
//            "CASE WHEN CAST(:currentTime AS TIME) NOT BETWEEN CAST(r.openTime AS TIME) AND CAST(r.closeTime AS TIME) THEN 2 ELSE 0 END")
//    Page<Restaurant> findAllWithCustomSorting(@Param("currentTime") LocalTime currentTime, Pageable pageable);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.isVerified = true " +
            "ORDER BY " +
            "CASE " +
            "   WHEN r.status = 'AVAILABLE' THEN 1 " +
            "   WHEN r.status = 'NOT_DELIVERABLE' THEN 2 " +
            "   WHEN r.status = 'NOT_OPEN' THEN 3 " +
            "   WHEN r.status = 'NOT_AVAILABLE' THEN 4 " +
            "   ELSE 5 " +
            "END")
    Page<Restaurant> findAllWithCustomSorting(Pageable pageable);

//    @Query("SELECT r FROM Restaurant r " +
//            "WHERE r.isVerified = true " +
//            "AND r.isVeg = :isVeg " +
//            "ORDER BY " +
//            "CASE WHEN r.isAvailable = false THEN 1 ELSE 0 END, " +
//            "CASE WHEN CAST(:currentTime AS TIME) NOT BETWEEN CAST(r.openTime AS TIME) AND CAST(r.closeTime AS TIME) THEN 2 ELSE 0 END")
//    Page<Restaurant> findAllWithIsVegFilter(@Param("currentTime") LocalTime currentTime, @Param("isVeg") boolean isVeg, Pageable pageable);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.isVerified = true " +
            "AND r.isVeg = :isVeg " +
            "ORDER BY " +
            "CASE " +
            "   WHEN r.status = 'AVAILABLE' THEN 1 " +
            "   WHEN r.status = 'NOT_DELIVERABLE' THEN 2 " +
            "   WHEN r.status = 'NOT_OPEN' THEN 3 " +
            "   WHEN r.status = 'NOT_AVAILABLE' THEN 4 " +
            "   ELSE 5 " +
            "END")
    Page<Restaurant> findAllWithIsVegFilter(@Param("isVeg") boolean isVeg, Pageable pageable);
}
