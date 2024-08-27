package com.app.devGrill.service;

import com.app.devGrill.entity.Ingredient;
import com.app.devGrill.entity.Menu;
import com.app.devGrill.repository.IngredientRepository;
import com.app.devGrill.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/new-ingredient")
    public Ingredient newIngredient(@RequestBody Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @DeleteMapping("/delete-ingredient/{idIngredient}")
    public void deleteIngredient(@PathVariable Integer idIngredient) {
        ingredientRepository.deleteById(idIngredient);
    }

    @PutMapping("/update-ingredient/{idIngredient}")
    public Ingredient updateStock(@PathVariable Integer idIngredient, @RequestBody Ingredient ingredient) {
        Ingredient ingredientSavedDB = ingredientRepository.findById(idIngredient)
                .orElseThrow(() -> new EntityNotFoundException("ingredient not found"));

        ingredientSavedDB.setStock(ingredient.getStock());
        Ingredient ingSavedDB = ingredientRepository.save(ingredientSavedDB);

        if(!ingSavedDB.getMenuIngredientListList().isEmpty()) {
            ingSavedDB.getMenuIngredientListList().forEach(record -> {
                ArrayList<Boolean> checkMenuAvailability = new ArrayList<>();
                Menu menuAssociatedIngredient = menuRepository.findById(record.getIdMenu())
                        .orElseThrow(() -> new EntityNotFoundException("menu not found"));

                if(!menuAssociatedIngredient.getIsAvailable()) {
                    menuAssociatedIngredient.getMenuIngredientListList().forEach(recordInList -> {
                        Ingredient ingredientFound = ingredientRepository.findById(recordInList.getIdIngredient())
                                .orElseThrow(() -> new EntityNotFoundException("ingredient not found"));

                        if (ingredientFound.getStock() < 1 || recordInList.getQuantity() > ingredientFound.getStock()) {
                            checkMenuAvailability.add(false);
                        } else if(ingredient.getStock() >= 1 && ingredient.getStock() >= recordInList.getQuantity()) {
                            checkMenuAvailability.add(true);
                        }
                    });

                    if(!checkMenuAvailability.contains(false)) {
                        menuAssociatedIngredient.setIsAvailable(true);
                        menuRepository.save(menuAssociatedIngredient);
                    }
                }
            });
        }

        return ingSavedDB;
    }

}