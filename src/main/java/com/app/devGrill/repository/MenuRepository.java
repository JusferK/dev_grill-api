package com.app.devGrill.repository;

import com.app.devGrill.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface MenuRepository extends JpaRepository<Menu, Serializable> {
    public Menu findByNameAndDescriptionAndPrice(String name, String Description, float price);
}
