package com.app.devGrill.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    @Serial
    private static final long serialVersionUID = 7L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idingredient")
    private Integer idIngredient;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private Integer stock;

    @OneToMany(mappedBy = "idIngredient", fetch = FetchType.LAZY)
    private List<MenuIngredientList> MenuIngredientListList = new ArrayList<>();

    public Integer getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(Integer idIngredient) {
        this.idIngredient = idIngredient;
    }

    public List<MenuIngredientList> getMenuIngredientListList() {
        return MenuIngredientListList;
    }

    public void setMenuIngredientListList(List<MenuIngredientList> menuIngredientListList) {
        MenuIngredientListList = menuIngredientListList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "   idIngredient: " + idIngredient + ", \n" +
                "   name: " + name + ", \n" +
                "   stock: " + stock + " \n" +
                "}";
    }
}
