package com.app.devGrill.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order_request")
public class OrderRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idorder_request")
    private Integer idOrderRequest;

    @Column(name = "order_date_time")
    private Date orderDateTime;

    @Column(name = "total_due")
    private Float totalDue;

    @Column(name = "status")
    private String status;

    @Column(name = "last_status_update")
    private Date lastStatusUpdate;

    @Column(name = "user_email")
    private String userEmail;

    @OneToMany(mappedBy = "orderRequestIdOrderRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MenuOrder> menuOrderList = new ArrayList<>();

    public Integer getIdOrderRequest() {
        return idOrderRequest;
    }

    public void setIdOrderRequest(Integer idOrderRequest) {
        this.idOrderRequest = idOrderRequest;
    }

    public Date getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public void setLastStatusUpdate(Date lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }

    public List<MenuOrder> getMenuOrderList() {
        return menuOrderList;
    }

    public void setMenuOrderList(List<MenuOrder> menuOrderList) {
        this.menuOrderList = menuOrderList;
    }

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Float totalDue) {
        this.totalDue = totalDue;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "   idOrderRequest: " + idOrderRequest + ", \n" +
                "   orderDateTime: " + orderDateTime + ", \n" +
                "   totalDue: " + totalDue + ", \n" +
                "   status: " + status + ", \n" +
                "   lastStatusUpdate: " + lastStatusUpdate + ", \n" +
                "   userEmail: " + userEmail + " \n" +
                "}";
    }
}
