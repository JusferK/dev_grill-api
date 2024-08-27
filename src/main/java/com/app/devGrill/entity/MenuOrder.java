package com.app.devGrill.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "menu_order")
public class MenuOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmenu_order")
    private Integer idMenuOrder;

    @Column(name = "quantity")
    private Integer quantity;


    @Column(name = "order_request_idorder_request")
    private Integer orderRequestIdOrderRequest;

    @Column(name = "menu_idmenu")
    private Integer menuIdMenu;

    public Integer getIdMenuOrder() {
        return idMenuOrder;
    }

    public void setIdMenuOrder(Integer idMenuOrder) {
        this.idMenuOrder = idMenuOrder;
    }

    public Integer getMenuIdMenu() {
        return menuIdMenu;
    }

    public void setMenuIdMenu(Integer menuIdMenu) {
        this.menuIdMenu = menuIdMenu;
    }

    public Integer getOrderRequestIdOrderRequest() {
        return orderRequestIdOrderRequest;
    }

    public void setOrderRequestIdOrderRequest(Integer orderRequestIdOrderRequest) {
        this.orderRequestIdOrderRequest = orderRequestIdOrderRequest;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
