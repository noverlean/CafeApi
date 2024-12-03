package com.noverlin.cafeapi.mapper;

import com.noverlin.cafeapi.entity.Dish;
import org.mapstruct.Mapper;
import org.openapitools.model.DishDto;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DishMapper {
    Dish dtoToModel(DishDto dishDto);
    DishDto modelToDto(Dish dish);
}
