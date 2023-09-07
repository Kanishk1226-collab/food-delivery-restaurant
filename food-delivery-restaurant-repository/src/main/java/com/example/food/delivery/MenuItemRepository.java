package com.example.food.delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Override
    boolean existsById(Integer integer);

    boolean existsByMenuRestIdAndMenuItemNameIgnoreCase(int menuRestId, String menuItemName);

    @Override
    Optional<MenuItem> findById(Integer integer);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.menuRestId = :menuRestId")
    Page<MenuItem> findByMenuRestId(@Param("menuRestId") int menuRestId, Pageable pageable);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.menuRestId = :menuRestId AND mi.isVeg = :isVeg")
    Page<MenuItem> findByMenuRestIdIsVegFilter(@Param("menuRestId") int menuRestId, @Param("isVeg") boolean isVeg, Pageable pageable);

    String findMenuItemNameByMenuItemId(int menuItemId);

}
