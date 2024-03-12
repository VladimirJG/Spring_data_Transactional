package ru.danilov.Spring_data_Transactional.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.danilov.Spring_data_Transactional.dto.OrderDto;
import ru.danilov.Spring_data_Transactional.service.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping
    public void placeOrder(@RequestBody OrderDto orderDto) {
        shopService.placeOrder(orderDto);
    }
}