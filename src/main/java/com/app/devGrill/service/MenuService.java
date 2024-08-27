package com.app.devGrill.service;

import com.app.devGrill.entity.Administrator;
import com.app.devGrill.entity.Ingredient;
import com.app.devGrill.entity.Menu;
import com.app.devGrill.entity.MenuIngredientList;
import com.app.devGrill.repository.IngredientRepository;
import com.app.devGrill.repository.MenuIngredientListRepository;
import com.app.devGrill.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuIngredientListRepository menuIngredientListRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping("/all-menus")
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @PostMapping("/new-menu")
    public <T> Object newMenu(@RequestBody Menu menu) {
        Menu getMenuDB = menuRepository.findByNameAndDescriptionAndPrice(menu.getName(), menu.getDescription(), menu.getPrice());
        if(!menu.getMenuIngredientListList().isEmpty() && getMenuDB == null) {
            ArrayList<Boolean> checkMenuAvailability = new ArrayList<>();
            Menu savedMenu = menuRepository.save(menu);
            for(MenuIngredientList item : savedMenu.getMenuIngredientListList()) {
                item.setIdMenu(savedMenu.getIdMenu());
                Ingredient ingredient = ingredientRepository.findByName(item.getIngredientName());

                if(ingredient != null) {
                    if(ingredient.getStock() < 1 || item.getQuantity() > ingredient.getStock()) {
                        checkMenuAvailability.add(false);
                    } else if(ingredient.getStock() >= 1 && ingredient.getStock() >= item.getQuantity()) {
                        checkMenuAvailability.add(true);
                    }

                    item.setIdIngredient(ingredient.getIdIngredient());
                    MenuIngredientList newMenuSavedInDb = menuIngredientListRepository.save(item);
                    ingredient.getMenuIngredientListList().add(newMenuSavedInDb);
                    ingredientRepository.save(ingredient);
                } else {
                    checkMenuAvailability.add(false);
                    ingredient = new Ingredient();
                    ingredient.setName(item.getIngredientName());
                    ingredient.setStock(0);
                    Ingredient newIngredientSavedInDb = ingredientRepository.save(ingredient);
                    item.setIdIngredient(newIngredientSavedInDb.getIdIngredient());
                    MenuIngredientList itemSaved = menuIngredientListRepository.save(item);
                    newIngredientSavedInDb.getMenuIngredientListList().add(itemSaved);
                    ingredientRepository.save(newIngredientSavedInDb);
                }
            }

            if(checkMenuAvailability.contains(false)) {
                savedMenu.setIsAvailable(false);
            } else {
                savedMenu.setIsAvailable(true);
            }

            return menuRepository.save(savedMenu);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Exception");

            if(menu.getMenuIngredientListList().isEmpty() && getMenuDB != null) {
                response.put("message", "request parameters no met");
            } else if(getMenuDB != null) {
                response.put("message", "Menu has been already added");
            } else if(menu.getMenuIngredientListList().isEmpty()) {
                response.put("message", "request parameters no met");
            }

            response.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-menu/{idMenu}")
    public void deleteMenu(@PathVariable Integer idMenu) {
        menuRepository.deleteById(idMenu);
    }

}