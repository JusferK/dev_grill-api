package com.app.devGrill.service;

import com.app.devGrill.entity.AdministratorType;
import com.app.devGrill.repository.AdministratorRepository;
import com.app.devGrill.repository.AdministratorTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator-type")
@CrossOrigin
public class AdministratorTypeService {


    @Autowired
    AdministratorTypeRepository administratorTypeRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @GetMapping("/get-administrators-type")
    public List<AdministratorType> getAdministratorsType() {
        return administratorTypeRepository.findAll();
    }

    @PostMapping("/new-administrator-type")
    public AdministratorType newAdministratorType(@RequestBody AdministratorType adminType) {
        return administratorTypeRepository.save(adminType);
    }

    @DeleteMapping("/delete-admin-type/{idAdministratorType}")
    public void deleteAdminType(@PathVariable Integer idAdministratorType) {
        administratorTypeRepository.deleteById(idAdministratorType);
    }

}
