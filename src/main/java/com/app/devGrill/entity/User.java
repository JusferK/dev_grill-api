 package com.app.devGrill.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "nit")
    private Integer nit;

    @Column(name = "sign_date")
    private Date signDate;

	@OneToMany(mappedBy = "userEmail", fetch = FetchType.LAZY)
	private List<OrderRequest> orderRequestList = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNit() {
        return nit;
    }

    public void setNit(Integer nit) {
        this.nit = nit;
    }

    public List<OrderRequest> getOrderRequestList() {
        return orderRequestList;
    }

    public void setOrderRequestList(List<OrderRequest> orderRequestList) {
        this.orderRequestList = orderRequestList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "   email: " + email + ", \n" +
                "   name: " + name + ", \n" +
                "   lastName: " + lastName + ", \n" +
                "   password: " + password + ", \n" +
                "   nit: " + nit + ", \n" +
                "   signDate: " + signDate + ", \n" +
                "   orderRequestList:" + orderRequestList + " \n" +
                "}";
    }
}
