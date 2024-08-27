package com.app.devGrill.repository;

import com.app.devGrill.entity.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Integer> {
    public List<OrderRequest> findByIdOrderRequest(Integer idOrderRequest);
    public List<OrderRequest> findByUserEmail(String email);
}