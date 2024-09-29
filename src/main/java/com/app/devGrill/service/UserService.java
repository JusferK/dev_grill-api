package com.app.devGrill.service;

import com.app.devGrill.entity.OrderRequest;
import com.app.devGrill.entity.User;
import com.app.devGrill.repository.OrderRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.devGrill.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/login")
    public <T> Object login(@RequestBody User user) {
        User userLogginAttempt = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

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
    public <T> Object signIn(@RequestBody User user) {

        User findUser = userRepository.findByEmail(user.getEmail());

        if(findUser == null) {
            return userRepository.save(user);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Exception");
            response.put("message", "Email is already registered.");
            response.put("status", HttpStatus.FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }

    }

    @DeleteMapping("/delete-account/{email}")
    public void deleteAccount(@PathVariable String email) {
        userRepository.deleteById(email);
    }

    @PutMapping("/update-user")
    public User updateUser(@RequestBody User user) {
        User userSavedDB = userRepository.findByEmail(user.getEmail());

        if(userSavedDB != null) {
            if(!user.getName().isEmpty()) userSavedDB.setName(user.getName());
            if(!user.getLastName().isEmpty()) userSavedDB.setLastName(user.getLastName());
            if(!user.getPassword().isEmpty()) userSavedDB.setPassword(user.getPassword());
            if(user.getNit() != null) userSavedDB.setNit(user.getNit());
        }

        return  userRepository.save(userSavedDB);
    }

}
