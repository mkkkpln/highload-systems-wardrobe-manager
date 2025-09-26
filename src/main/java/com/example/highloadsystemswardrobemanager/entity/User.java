package com.example.highloadsystemswardrobemanager.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WardrobeItem> items;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Outfit> outfits;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<WardrobeItem> getItems() { return items; }
    public void setItems(List<WardrobeItem> items) { this.items = items; }

    public List<Outfit> getOutfits() { return outfits; }
    public void setOutfits(List<Outfit> outfits) { this.outfits = outfits; }
}
