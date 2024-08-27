package com.app.devGrill.repository;

import com.app.devGrill.entity.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface MenuOrderRepository extends JpaRepository<MenuOrder, Serializable> {

}
