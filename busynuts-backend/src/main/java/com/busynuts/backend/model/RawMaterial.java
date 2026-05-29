package com.busynuts.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "raw_materials")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We will store the username of the seller who submitted this batch
    @Column(nullable = false)
    private String sellerUsername;

    // E.g., "Peanut Cake", "Sesame Seeds"
    @Column(nullable = false)
    private String materialType;

    @Column(nullable = false)
    private Double weightInKg;

    @Column(nullable = false)
    private Double askingPrice;

    // Status can be: PENDING, APPROVED, REJECTED, PAID
    @Column(nullable = false)
    private String status = "PENDING"; 

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public Double getWeightInKg() { return weightInKg; }
    public void setWeightInKg(Double weightInKg) { this.weightInKg = weightInKg; }

    public Double getAskingPrice() { return askingPrice; }
    public void setAskingPrice(Double askingPrice) { this.askingPrice = askingPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}