package com.app.devGrill.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    @Serial
    private static final long serialVersionUID = 6L;

    @Id
    @Basic(optional = false)
    @Column(name = "idmenu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMenu;

    @Column(name = "price")
    private Float price;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Lob
    @Column(name = "photo")
    private String photo;

    @OneToMany(mappedBy = "idMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MenuIngredientList> menuIngredientListList = new ArrayList<>();

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public List<MenuIngredientList> getMenuIngredientListList() {
        return menuIngredientListList;
    }

    public void setMenuIngredientListList(List<MenuIngredientList> menuIngredientListList) {
        this.menuIngredientListList = menuIngredientListList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "   idMenu: " + idMenu + ", \n" +
                "   price: " + price + ", \n" +
                "   name: " + name + ", \n" +
                "   description:" + description + ", \n" +
                "   isAvailable: " + isAvailable + ", \n" +
                "   photo:" + photo + ", \n" +
                "   menuIngredientListList:" + menuIngredientListList + " \n" +
                "}";
    }
}