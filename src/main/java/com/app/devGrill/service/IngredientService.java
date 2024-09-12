package com.app.devGrill.service;

import com.app.devGrill.entity.Ingredient;
import com.app.devGrill.entity.Menu;
import com.app.devGrill.repository.IngredientRepository;
import com.app.devGrill.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ingredients")
@CrossOrigin
public class IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    MenuRepository menuRepository;

    @GetMapping("/all-ingredients")
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/get-ingredient/{idIngredient}")
    public Ingredient getIngredient(@PathVariable Integer idIngredient) {
        return ingredientRepository.findById(idIngredient).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    @PostMapping("/new-ingredient")
    public <T> Object newIngredient(@RequestBody Ingredient ingredient) {
        Ingredient findIngredient = ingredientRepository.findByName(ingredient.getName());

        if(findIngredient == null) {
            return ingredientRepository.save(ingredient);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Exception");
            response.put("message", "Ingredient is already on records");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-ingredient/{idIngredient}")
    public void deleteIngredient(@PathVariable Integer idIngredient) {
        ingredientRepository.deleteById(idIngredient);
    }

    @PutMapping("/update-ingredient/{idIngredient}")
    public <T> Object updateStock(@PathVariable Integer idIngredient, @RequestBody Ingredient ingredient) {

        Ingredient findIngredientByName = ingredientRepository.findByName(ingredient.getName());
        Ingredient ingredientById = ingredientRepository.findById(idIngredient)
                .orElse(null);

        if (findIngredientByName == null) {
            ingredientById.setStock(ingredient.getStock());
            ingredientById.setName(ingredient.getName());
            Ingredient ingredientSavedDB = ingredientRepository.save(ingredientById);

            if (!ingredientSavedDB.getMenuIngredientListList().isEmpty()) {
                ingredientSavedDB.getMenuIngredientListList().forEach(record -> {
                    ArrayList<Boolean> checkMenuAvailability = new ArrayList<>();
                    Menu menuAssociatedIngredient = menuRepository.findById(record.getIdMenu())
                            .orElseThrow(() -> new EntityNotFoundException("menu not found"));

                    menuAssociatedIngredient.getMenuIngredientListList().forEach(recordInList -> {
                        Ingredient ingredientFound = ingredientRepository.findById(recordInList.getIdIngredient())
                                .orElseThrow(() -> new EntityNotFoundException("ingredient not found"));

                        if (ingredientFound.getStock() < 1 || recordInList.getQuantity() > ingredientFound.getStock()) {
                            checkMenuAvailability.add(false);
                        } else if (ingredientFound.getStock() >= 1 && ingredientFound.getStock() >= recordInList.getQuantity()) {
                            checkMenuAvailability.add(true);
                        }
                    });

                    System.out.println(checkMenuAvailability);

                    if (!checkMenuAvailability.contains(false)) {
                        menuAssociatedIngredient.setIsAvailable(true);
                        menuRepository.save(menuAssociatedIngredient);
                    } else {
                        menuAssociatedIngredient.setIsAvailable(false);
                        menuRepository.save(menuAssociatedIngredient);
                    }
                });
            }
            return ingredientSavedDB;
        } else {
            Ingredient ingredientToReturn = null;
            Map<String, Object> response = null;
            boolean namesAreEqual = ingredientById.getName().equalsIgnoreCase(ingredient.getName());

            if (namesAreEqual) {
                ingredientById.setStock(ingredient.getStock());
                ingredientById.setName(ingredient.getName());
                ingredientToReturn = ingredientRepository.save(ingredientById);

                ingredientToReturn.getMenuIngredientListList().forEach(record -> {
                    ArrayList<Boolean> checkMenuAvailability = new ArrayList<>();
                    Menu menuAssociatedIngredient = menuRepository.findById(record.getIdMenu())
                            .orElseThrow(() -> new EntityNotFoundException("menu not found"));

                    menuAssociatedIngredient.getMenuIngredientListList().forEach(recordInList -> {
                        Ingredient ingredientFound = ingredientRepository.findById(recordInList.getIdIngredient())
                                .orElseThrow(() -> new EntityNotFoundException("ingredient not found"));

                        if (ingredientFound.getStock() < 1 || recordInList.getQuantity() > ingredientFound.getStock()) {
                            checkMenuAvailability.add(false);
                        } else if (ingredientFound.getStock() >= 1 && ingredientFound.getStock() >= recordInList.getQuantity()) {
                            checkMenuAvailability.add(true);
                        }
                    });

                    if (!checkMenuAvailability.contains(false)) {
                        menuAssociatedIngredient.setIsAvailable(true);
                        menuRepository.save(menuAssociatedIngredient);
                    } else {
                        menuAssociatedIngredient.setIsAvailable(false);
                        menuRepository.save(menuAssociatedIngredient);
                    }
                });
            } else if (!namesAreEqual && findIngredientByName.getName().equalsIgnoreCase(ingredient.getName()) && ingredientById.getIdIngredient() != findIngredientByName.getIdIngredient()) {
                response = new HashMap<>();
                response.put("error", "Exception");
                response.put("message", "Ingredient is already on records");
                response.put("status", HttpStatus.BAD_REQUEST.value());
            }
            return ingredientToReturn != null ? ingredientToReturn : response;
        }
    }
}