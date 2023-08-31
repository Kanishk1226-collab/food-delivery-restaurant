package com.example.food.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuTypeRepository extends JpaRepository<MenuType, Integer> {
    boolean existsByMenuTypeNameIgnoreCase(String menuTypeName);

    @Override
    Optional<MenuType> findById(Integer integer);

    @Override
    boolean existsById(Integer integer);
}
