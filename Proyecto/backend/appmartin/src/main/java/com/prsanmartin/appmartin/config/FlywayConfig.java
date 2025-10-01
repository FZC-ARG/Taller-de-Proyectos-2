package com.prsanmartin.appmartin.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy repairAndMigrateStrategy() {
        return new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                try {
                    // Attempt to repair failed/dirty migrations first
                    flyway.repair();
                } catch (Exception ignored) {
                    // Continue even if repair throws due to version incompatibilities
                }
                flyway.migrate();
            }
        };
    }
}


