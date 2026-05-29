package com.busynuts.backend.repository;

import com.busynuts.backend.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    // Custom method to fetch lots belonging only to a specific logged-in seller
    List<RawMaterial> findBySellerUsername(String sellerUsername);
}