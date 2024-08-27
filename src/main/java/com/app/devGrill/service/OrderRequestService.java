package com.app.devGrill.service;

import com.app.devGrill.entity.*;
import com.app.devGrill.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderRequestService {

    @Autowired
    OrderRequestRepository orderRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MenuOrderRepository menuOrderRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    MenuRepository menuRepository;

    @GetMapping("/all-orders-placed")
    public List<OrderRequest> ordersMade() {
        return orderRequestRepository.findAll();
    }

    @GetMapping("/find-orders/{parameter}")
    public <T> List<OrderRequest> findSpecificOrders(@PathVariable T parameter) {
        boolean isNumber = true;
        String parameterToString = null;
        Integer orderidOrNit = null;

        try {
            parameterToString = (String) parameter;
            orderidOrNit = Integer.parseInt(parameterToString);
        } catch (NumberFormatException e) {
            isNumber = false;
        }


        if(isNumber) {
            List<OrderRequest> listByOrderId = orderRequestRepository.findByIdOrderRequest(orderidOrNit);
            User foundUser = userRepository.findByNit(orderidOrNit);
            String email = null;

            if(foundUser != null) email = foundUser.getEmail();
            List<OrderRequest> listByNit = orderRequestRepository.findByUserEmail(email);

            return foundUser != null ? listByNit : listByOrderId;
        } else {
            User foundUser = userRepository.findByName(parameterToString);
            return orderRequestRepository.findByUserEmail(foundUser.getEmail());
        }
    }

    @PostMapping("/new-order")
    public <T> Object newOrder(@RequestBody OrderRequest orderRequest) {
        User userPlacedOrder = userRepository.findByEmail(orderRequest.getUserEmail());
        boolean[] orderCanBeProcess = {false};
        ResponseEntity<Map> responseEntity = null;
        OrderRequest orderToBeReturn = null;

        if(userPlacedOrder != null && !orderRequest.getMenuOrderList().isEmpty()) {
            OrderRequest orderSavedDB = orderRequestRepository.save(orderRequest);
            int[] i = {0};
            orderSavedDB.getMenuOrderList().forEach((menuOrder) -> {

                ArrayList<Boolean> checkAvailability = new ArrayList<>();
                Menu findMenu = menuRepository.findById(menuOrder.getMenuIdMenu())
                        .orElseThrow(() -> new EntityNotFoundException("menu not found"));

                findMenu.getMenuIngredientListList().forEach(menuIngredientListRecord -> {

                    Ingredient findIngredient = ingredientRepository.findById(menuIngredientListRecord.getIdIngredient())
                            .orElseThrow(() -> new EntityNotFoundException("no ingredient found"));

                    int stockToBeConsumed = menuOrder.getQuantity() * menuIngredientListRecord.getQuantity();

                    if (stockToBeConsumed > findIngredient.getStock()) {
                        checkAvailability.add(false);
                    } else if(findIngredient.getStock() > stockToBeConsumed) {
                        checkAvailability.add(true);
                    }
                });

                if(!checkAvailability.contains(false)) {
                    orderCanBeProcess[0] = true;
                    menuOrder.setOrderRequestIdOrderRequest(orderSavedDB.getIdOrderRequest());
                    MenuOrder recordSaved = menuOrderRepository.save(menuOrder);
                    orderSavedDB.getMenuOrderList().set(i[0], recordSaved);
                    i[0]++;
                }
            });

            if(orderCanBeProcess[0]) {
                userPlacedOrder.getOrderRequestList().add(orderSavedDB);
                userRepository.save(userPlacedOrder);
                orderToBeReturn = orderSavedDB;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Exception");
                response.put("message", "There is no availability or it was a bad request");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        return orderCanBeProcess[0] ? orderToBeReturn : responseEntity;
    }

    @DeleteMapping("/delete-order/{idOrderRequest}")
    public void deleteOrder(@PathVariable Integer idOrderRequest) {
        orderRequestRepository.deleteById(idOrderRequest);
    }

    @PutMapping("/update-order-status/{idOrderRequest}")
    public <T> Object updateStatus(@PathVariable Integer idOrderRequest, @RequestBody OrderRequest orderRequest) {
        OrderRequest orderSavedDB = orderRequestRepository.findById(idOrderRequest)
                .orElseThrow(() -> new EntityNotFoundException("order not found"));

        ResponseEntity<Map> responseEntity = null;

        if (orderRequest.getStatus().equalsIgnoreCase("completed") && orderSavedDB != null && orderSavedDB.getStatus().equalsIgnoreCase("in progress")) {
            orderSavedDB.getMenuOrderList().forEach(record -> {
                Menu findMenu = menuRepository.findById(record.getMenuIdMenu())
                        .orElseThrow(() -> new EntityNotFoundException("menu not found"));

                findMenu.getMenuIngredientListList().forEach(milRecord -> {
                    Integer amountOfIngredientsUsed = record.getQuantity() * milRecord.getQuantity();

                    Ingredient ingredient = ingredientRepository.findById(milRecord.getIdIngredient())
                            .orElseThrow(() -> new EntityNotFoundException("ingredient not found"));

                    ingredient.setStock(ingredient.getStock() - amountOfIngredientsUsed);
                    ingredientRepository.save(ingredient);
                });
            });
            orderSavedDB.setStatus(orderRequest.getStatus());
            orderSavedDB.setLastStatusUpdate(orderRequest.getLastStatusUpdate());
            orderSavedDB = orderRequestRepository.save(orderSavedDB);
        } else if(!orderRequest.getStatus().equalsIgnoreCase("completed") && !orderSavedDB.getStatus().equalsIgnoreCase("cancelled")) {
            orderSavedDB.setStatus(orderRequest.getStatus());
            orderSavedDB.setLastStatusUpdate(orderRequest.getLastStatusUpdate());
            orderSavedDB = orderRequestRepository.save(orderSavedDB);
        } else {
            orderSavedDB = null;
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Exception");
            response.put("message", "status could not be understood by server");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            responseEntity =  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return orderSavedDB != null ? orderSavedDB : responseEntity;
    }
}
