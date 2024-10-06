package com.app.devGrill.repository;

import com.app.devGrill.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    public Provider findByNameAndNit(String name, Integer nit);
    public Provider findByName(String name);
}
