package com.app.devGrill.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "administrator")
public class Administrator implements Serializable {

    @Serial
    private static final long serialVersionUID = 8L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idadministrator")
    private Integer idAdministrator;

    @Column(name = "user")
    private String user;

    @Column(name = "password")
    private String password;

    @Column(name = "administrator_type_idadministrator_type")
    private Integer administratorTypeIdAdministratorType;

    public Integer getIdAdministrator() {
        return idAdministrator;
    }

    public void setIdAdministrator(Integer idAdministrator) {
        this.idAdministrator = idAdministrator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getAdministratorTypeIdAdministratorType() {
        return administratorTypeIdAdministratorType;
    }

    public void setAdministratorTypeIdAdministratorType(Integer administratorTypeIdAdministratorType) {
        this.administratorTypeIdAdministratorType = administratorTypeIdAdministratorType;
    }
}
