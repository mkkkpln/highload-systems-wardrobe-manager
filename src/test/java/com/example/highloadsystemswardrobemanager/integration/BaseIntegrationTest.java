package com.example.highloadsystemswardrobemanager.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE outfits CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE wardrobe_items CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");
    }
}

