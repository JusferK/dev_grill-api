package com.app.devGrill.repository;

import com.app.devGrill.entity.Ingredient;
import com.app.devGrill.entity.MenuIngredientList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface MenuIngredientListRepository extends JpaRepository<MenuIngredientList, Serializable> {
}