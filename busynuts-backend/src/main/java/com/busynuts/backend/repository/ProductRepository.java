package com.busynuts.backend.repository;

import com.busynuts.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository gives us save(), findAll(), deleteById() completely for free!
}