package com.fellasbar.api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("postgres")
public class SchemaUpdater {

    private final JdbcTemplate jdbcTemplate;

    public SchemaUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void applyMigrations() {
        try {
            jdbcTemplate.execute(
                "ALTER TABLE business_users ALTER COLUMN password DROP NOT NULL"
            );
            System.out.println("[SchemaUpdater] Made business_users.password nullable.");
        } catch (Exception e) {
            // Already nullable or column doesn't exist — safe to ignore
        }
    }
}
