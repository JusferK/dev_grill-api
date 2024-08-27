package com.app.devGrill.service;

import com.app.devGrill.entity.MenuOrder;
import com.app.devGrill.repository.MenuOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("menu-order")
@CrossOrigin
public class MenuOrderService {

    @Autowired
    MenuOrderRepository menuOrderRepository;

    @GetMapping("/get-all-menu-order")
    public List<MenuOrder> getAllMenuOrders() {
        return menuOrderRepository.findAll();
    }

}
