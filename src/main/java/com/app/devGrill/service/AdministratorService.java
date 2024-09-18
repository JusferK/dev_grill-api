package com.app.devGrill.service;

import com.app.devGrill.entity.Administrator;
import com.app.devGrill.repository.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator")
@CrossOrigin
public class AdministratorService {

    @Autowired
    AdministratorRepository administratorRepository;

    @GetMapping("/get-admins")
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    @PostMapping("/new-admin")
    public Administrator newAdmin(@RequestBody Administrator admin) {
        return administratorRepository.save(admin);
    }

    @DeleteMapping("/delete-admin/{idAdministrator}")
    public void deleteAdmin(@PathVariable Integer idAdministrator) {
        administratorRepository.deleteById(idAdministrator);
    }

    @PostMapping("/admin-login/{user}/{password}")
    public <T> Object adminLogin(@PathVariable String user, @PathVariable String password) {
        Administrator loginAttempt = administratorRepository.findByUserAndPassword(user, password);

        if(loginAttempt != null && loginAttempt.getUser().equals(user) && loginAttempt.getPassword().equals(password)) {
            return loginAttempt;
        } else {
            return false;
        }
    }

    @PutMapping("/update-admin/{idAdministrator}")
    public Administrator updateAdmin(@PathVariable Integer idAdministrator, @RequestBody Administrator admin) {
        Administrator adminSavedDB = administratorRepository.findById(idAdministrator)
                .orElseThrow(() -> new EntityNotFoundException("admin account not found"));

        if(!admin.getPassword().isEmpty()) adminSavedDB.setPassword(admin.getPassword());

        return administratorRepository.save(adminSavedDB);
    }

}
