package com.app.devGrill.service;

import com.app.devGrill.entity.Provider;
import com.app.devGrill.repository.PhoneRepository;
import com.app.devGrill.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/provider")
@CrossOrigin
public class ProviderService {

    @Autowired
    ProviderRepository pr;

    @Autowired
    PhoneRepository phoneRepository;

    @GetMapping("/providers")
    public List<Provider> getAllProviders() {
        return pr.findAll();
    }

    @GetMapping("/get-provider/{name}/{nit}")
    public <T> Object getProvider(@PathVariable String name, @PathVariable Integer nit) {
        Provider findProvider = pr.findByNameAndNit(name, nit);

        if(findProvider != null) {
            return findProvider;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Not found");
            response.put("message", "Provider was not found");
            response.put("status", HttpStatus.FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @PostMapping("/new-provider")
    public <T> Object newProvider(@RequestBody Provider provider) {

        Provider findProvider = pr.findByName(provider.getName());

        if(findProvider == null) {
            Provider providerToSave = pr.save(provider);

            if(provider.getPhone_list() != null) {
                if(!provider.getPhone_list().isEmpty()) {
                    provider.getPhone_list().forEach((phone) -> {
                        phone.setProvider_id(providerToSave.getProvider_id());
                        phoneRepository.save(phone);
                    });
                }
            } else {
                providerToSave.setPhone_list(new ArrayList<>());
            }

            return providerToSave;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Conflict");
            response.put("message", "Provider is already in records");
            response.put("status", HttpStatus.FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }

    }

    @DeleteMapping("/remove-provider/{provider_id}")
    public void deleteProvider(@PathVariable Integer provider_id) {
        pr.deleteById(provider_id);
    }

    @PutMapping("/update-provider")
    public <T> Object updateProviderInfo(@RequestBody Provider provider) {
        Optional<Provider> findProvider = pr.findById(provider.getProvider_id());
        if(findProvider.isPresent()) {
            Provider providerFound = findProvider.get();
            if(provider.getNit() != null) providerFound.setNit(provider.getNit());
            return pr.save(providerFound);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Conflict");
            response.put("message", "Provider not found");
            response.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

}
