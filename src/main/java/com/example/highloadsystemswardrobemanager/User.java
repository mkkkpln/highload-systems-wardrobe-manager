package com.example.highloadsystemswardrobemanager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    // Один пользователь владеет многими предметами
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // чтобы не было рекурсии в ответе
    private List<WardrobeItem> items;

    // Один пользователь имеет много образов
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // чтобы не было рекурсии в ответе
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
