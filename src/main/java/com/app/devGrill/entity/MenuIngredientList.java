package com.app.devGrill.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "menu_ingredient_list")
public class MenuIngredientList implements Serializable {

    @Serial
    private static final long serialVersionUID = 5L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmenu_ingredient_list")
    private Integer idMenuIngredientList;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "menu_idmenu")
    private Integer idMenu;

    @Column(name = "ingredient_idingredient")
    private Integer idIngredient;

    public Integer getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(Integer idIngredient) {
        this.idIngredient = idIngredient;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public Integer getIdMenuIngredientList() {
        return idMenuIngredientList;
    }

    public void setIdMenuIngredientList(Integer idMenuIngredientList) {
        this.idMenuIngredientList = idMenuIngredientList;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "   idMenuIngredientList: " + idMenuIngredientList + " \n" +
                "   quantity: " + quantity + ", \n" +
                "   ingredientName: " + ingredientName + ", \n" +
                "   idIngredient: " + idIngredient + " \n" +
                "   idMenu: " + idMenu + " \n" +
                "}";
    }
}