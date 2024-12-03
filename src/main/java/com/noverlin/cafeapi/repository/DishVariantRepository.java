package com.noverlin.cafeapi.repository;

import com.noverlin.cafeapi.entity.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends CrudRepository<Dish, Long> {
    List<Dish> findAll();
}
