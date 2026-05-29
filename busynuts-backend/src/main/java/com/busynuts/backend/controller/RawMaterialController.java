package com.busynuts.backend.controller;

import com.busynuts.backend.model.RawMaterial;
import com.busynuts.backend.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/raw-materials")
@CrossOrigin(origins = "*")
public class RawMaterialController {

    @Autowired
    private RawMaterialRepository repository;

    // 1. Get ALL materials (For the Admin Procurement Queue)
    @GetMapping
    public List<RawMaterial> getAllMaterials() {
        return repository.findAll();
    }

    // 2. Get materials for a specific seller (For the Seller Dashboard)
    @GetMapping("/seller/{username}")
    public List<RawMaterial> getMaterialsBySeller(@PathVariable String username) {
        return repository.findBySellerUsername(username);
    }

    // 3. Submit a new batch (Used by Sellers)
    @PostMapping
    public ResponseEntity<RawMaterial> submitMaterial(@RequestBody RawMaterial material) {
        material.setStatus("PENDING"); // Force status to pending on creation
        RawMaterial savedMaterial = repository.save(material);
        return ResponseEntity.ok(savedMaterial);
    }

    // 4. Update the status of a batch (Used by Admins to Approve/Reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        Optional<RawMaterial> optionalMaterial = repository.findById(id);
        
        if (optionalMaterial.isPresent()) {
            RawMaterial material = optionalMaterial.get();
            // Clean up the string in case JSON quotes are attached
            material.setStatus(newStatus.replace("\"", "")); 
            repository.save(material);
            return ResponseEntity.ok("Status updated successfully to: " + material.getStatus());
        }
        return ResponseEntity.notFound().build();
    }
}