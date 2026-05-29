package com.busynuts.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Tells MySQL to name the table "users"
public class User {

    @Id // Marks this as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true) // Cannot be null, and must be unique
    private String username;

    @Column(nullable = false) // Cannot be null
    private String password;

    @Enumerated(EnumType.STRING) // Saves the Role enum as a String in the database
    @Column(nullable = false)
    private Role role;

    // --- Getters and Setters ---
    // (In Java, we use these to safely access and update private variables)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}