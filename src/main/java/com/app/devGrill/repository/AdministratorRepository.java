package com.app.devGrill.repository;

import com.app.devGrill.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    public Administrator findByUserAndPassword(String user, String password);
}