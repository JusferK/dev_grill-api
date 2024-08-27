package com.app.devGrill.repository;

import com.app.devGrill.entity.OrderRequest;
import com.app.devGrill.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Serializable> {

    public User findByEmailAndPassword(String email, String password);
    public User findByEmail(String email);
    public User findByNit(int nit);
    public User findByName(String name);

}
