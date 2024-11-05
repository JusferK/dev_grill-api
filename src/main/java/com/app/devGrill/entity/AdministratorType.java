package com.app.devGrill.entity;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administrator_type")
public class AdministratorType implements Serializable {

    @Serial
    private static final long serialVersionUID = 9L;

    @Id
    @Basic(optional = false)
    @Column(name = "idadministrator_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAdministratorType;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private String photo;

    @OneToMany(mappedBy = "administratorTypeIdAdministratorType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Administrator> administratorTypeList = new ArrayList<>();

    public Integer getIdAdministratorType() {
        return idAdministratorType;
    }

    public void setIdAdministratorType(Integer idAdministratorType) {
        this.idAdministratorType = idAdministratorType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Administrator> getAdministratorTypeList() {
        return administratorTypeList;
    }

    public void setAdministratorTypeList(List<Administrator> administratorTypeList) {
        this.administratorTypeList = administratorTypeList;
    }
}
