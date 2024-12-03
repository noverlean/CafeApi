package com.noverlin.cafeapi.mapper;

import com.noverlin.cafeapi.entity.Dish;
import com.noverlin.cafeapi.entity.Order;
import org.mapstruct.Mapper;
import org.openapitools.model.DishDto;
import org.openapitools.model.OrderDto;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OrderMapper {
    Order dtoToModel(OrderDto orderDto);
    OrderDto modelToDto(Order order);
}
