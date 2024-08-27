package com.app.devGrill.repository;

import com.app.devGrill.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    public Ingredient findByName(String name);
}
