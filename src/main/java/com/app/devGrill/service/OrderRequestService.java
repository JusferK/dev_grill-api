package com.app.devGrill.service;

import com.app.devGrill.entity.*;
import com.app.devGrill.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public <T> Object findSpecificOrders(@PathVariable T parameter) {

        boolean isNumber = true;
        String parameterToString = null;
        Integer orderidOrNit = null;
        boolean wasFound = false;

        OrderRequest orderRequestToReturned = null;

        try {
            parameterToString = (String) parameter;
            orderidOrNit = Integer.parseInt(parameterToString);
        } catch (NumberFormatException e) {
            isNumber = false;
        }

        if(isNumber) {
            OrderRequest ByOrderId = orderRequestRepository.findById(orderidOrNit)
                    .orElse(null);
            User foundUser = userRepository.findByNit(orderidOrNit);
            String email = null;

            if(foundUser != null) email = foundUser.getEmail();
            Optional<OrderRequest> pivot = orderRequestRepository.findFirstByUserEmail(email);

            OrderRequest ByNit = null;

            if(pivot.isPresent()) {
                ByNit = pivot.get();
            }

            if(foundUser != null) {
                orderRequestToReturned = ByNit;
                wasFound = true;
            } else if(ByOrderId != null) {
                orderRequestToReturned = ByOrderId;
                wasFound = true;
            }
        } else {
            User foundUser = userRepository.findByName(parameterToString);
            if(foundUser != null) {
                Optional<OrderRequest> pivot = orderRequestRepository.findFirstByUserEmail(foundUser.getEmail());
                if(pivot.isPresent()) {
                    orderRequestToReturned = pivot.get();
                    wasFound = true;
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Exception");
        response.put("message", "Order not found");
        response.put("status", HttpStatus.NOT_FOUND.value());

        return wasFound ? orderRequestToReturned : new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new-order")
    public <T> Object newOrder(@RequestBody OrderRequest orderRequest) {
        User userPlacedOrder = userRepository.findByEmail(orderRequest.getUserEmail());
        ArrayList<Boolean> orderCanBeProcess = new ArrayList<>();
        boolean finalOrderCanBeProcess = false;
        ResponseEntity<Map> responseEntity = null;
        OrderRequest orderToBeReturn = null;
        ArrayList<MenuOrder> bag = new ArrayList<>();

        if(userPlacedOrder != null && !orderRequest.getMenuOrderList().isEmpty()) {
            OrderRequest[] orderSavedDB = new OrderRequest[1];
            orderRequest.getMenuOrderList().forEach((menuOrder) -> {

                ArrayList<Boolean> checkAvailability = new ArrayList<>();

                Optional<Menu> findMenu = menuRepository.findById(menuOrder.getMenuIdMenu());

                if(findMenu.isPresent()) {
                    Menu menuToIterate = findMenu.get();

                    menuToIterate.getMenuIngredientListList().forEach(menuIngredientListRecord -> {

                        Ingredient findIngredient = ingredientRepository.findById(menuIngredientListRecord.getIdIngredient())
                                .orElseThrow(() -> new EntityNotFoundException("no ingredient found"));

                        int stockToBeConsumed = menuOrder.getQuantity() * menuIngredientListRecord.getQuantity();

                        if (stockToBeConsumed > findIngredient.getStock()) {
                            checkAvailability.add(false);
                        } else if(findIngredient.getStock() > stockToBeConsumed) {
                            checkAvailability.add(true);
                        }
                    });
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Menu not found"));
                }

                if(!checkAvailability.contains(false)) {
                    orderCanBeProcess.add(true);
                    bag.add(menuOrder);
                } else {
                    orderCanBeProcess.add(false);
                }
            });

            if(!orderCanBeProcess.contains(false)) {
                finalOrderCanBeProcess = true;
                orderSavedDB[0] = orderRequestRepository.save(orderRequest);
                int[] i = {0};
                bag.forEach((menuOrder) -> {
                    menuOrder.setOrderRequestIdOrderRequest(orderSavedDB[0].getIdOrderRequest());
                    MenuOrder recordSaved = menuOrderRepository.save(menuOrder);
                    orderSavedDB[0].getMenuOrderList().set(i[0], recordSaved);
                    i[0]++;
                });

                userPlacedOrder.getOrderRequestList().add(orderSavedDB[0]);
                userRepository.save(userPlacedOrder);
                orderToBeReturn = orderSavedDB[0];
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Exception");
                response.put("message", "There is no availability or it was a bad request");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Exception");
            if(userPlacedOrder == null) response.put("message", "User not found");
            if(orderRequest.getMenuOrderList().isEmpty()) response.put("message", "Order is empty");
            if(userPlacedOrder == null && orderRequest.getMenuOrderList().isEmpty()) response.put("message","Request parameters are not met");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return finalOrderCanBeProcess ? orderToBeReturn : responseEntity;
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