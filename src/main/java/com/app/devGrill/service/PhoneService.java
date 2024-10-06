package com.app.devGrill.service;

import com.app.devGrill.entity.Phone;
import com.app.devGrill.entity.Provider;
import com.app.devGrill.repository.PhoneRepository;
import com.app.devGrill.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/phone")
@CrossOrigin
public class PhoneService {
    
    @Autowired
    PhoneRepository tr;

    @Autowired
    ProviderRepository providerRepository;
    
    @GetMapping("/all-phones")
    public List<Phone> getAllPhones() {
        return tr.findAll();
    }

    @PostMapping("/new-phone")
    public <T> Object newPhone(@RequestBody Phone phone) {
        if(phone.getProvider_id() != null && phone.getPhone() != null) {
            Optional<Provider> provider = providerRepository.findById(phone.getProvider_id());
            if(provider.isPresent()) {
                return tr.save(phone);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Conflict");
                response.put("message", "Provider not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Requirements not meet");
            response.put("message", "Phone need more information");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-phone/{phone_id}")
    public void deletePhone(@PathVariable Integer phone_id) {
        tr.deleteById(phone_id);
    }

    @PutMapping("/update-phone")
    public <T> Object updatePhoneInfo(@RequestBody Phone phone) {
        Optional<Phone> findPhone = tr.findById(phone.getPhone_id());
        if (findPhone.isPresent()) {
            Phone phoneFound = findPhone.get();

            if(phone.getPhone() != null) phoneFound.setPhone(phone.getPhone());

            return tr.save(phoneFound);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Conflict");
            response.put("message", "Phone not found");
            response.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }
    
}
