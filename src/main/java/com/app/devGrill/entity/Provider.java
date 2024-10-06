package com.app.devGrill.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "provider")
public class Provider {

    @Serial
    private static final long serialVersionUID = 9L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "provider_id")
    private Integer provider_id;

    @Column(name = "name")
    private String name;

    @Column(name = "nit")
    private Integer nit;

    @Column(name = "added_date")
    private Date added_date;

    @OneToMany(mappedBy = "provider_id",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Phone> phone_list;

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
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

    public Date getAdded_date() {
        return added_date;
    }

    public void setAdded_date(Date added_date) {
        this.added_date = added_date;
    }

    public List<Phone> getPhone_list() {
        return phone_list;
    }

    public void setPhone_list(List<Phone> phone_list) {
        this.phone_list = phone_list;
    }
}
