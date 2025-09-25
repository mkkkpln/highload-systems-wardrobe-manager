package com.example.highloadsystemswardrobemanager;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "outfits")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.Instant createdAt = java.time.Instant.now();


    @ManyToMany
    @JoinTable(
            name = "outfit_items",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<WardrobeItem> items = new HashSet<>();

    public Outfit() {}

    public Outfit(String title, User user) {
        this.title = title;
        this.user = user;
    }

    // 🔹 Геттеры/сеттеры
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<WardrobeItem> getItems() {
        return items;
    }

    public void setItems(Set<WardrobeItem> items) {
        this.items = items;
    }
}