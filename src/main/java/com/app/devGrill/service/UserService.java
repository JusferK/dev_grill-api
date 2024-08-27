package com.app.devGrill.service;

import com.app.devGrill.entity.Administrator;
import com.app.devGrill.entity.OrderRequest;
import com.app.devGrill.entity.User;
import com.app.devGrill.repository.OrderRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.app.devGrill.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRequestRepository orderRequestRepository;

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/login/{email}/{password}")
    public <T> Object login(@PathVariable("email") String email, @PathVariable("password") String password) {
        User userLogginAttempt = userRepository.findByEmailAndPassword(email, password);

        if(userLogginAttempt != null) {
            return userLogginAttempt;
        } else {
            return false;
        }
    }

    @GetMapping("/orders-made/{email}")
    public List<OrderRequest> getUserOrders(@PathVariable("email") String email) {
        return orderRequestRepository.findByUserEmail(email);
    }

    @PostMapping("/sign")
    public User signIn(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/delete-account/{email}")
    public void deleteAccount(@PathVariable String email) {
        userRepository.deleteById(email);
    }

    @PutMapping("/update-admin/{email}")
    public void updateAdmin(@PathVariable String email, @RequestBody User user) {

    }

}
