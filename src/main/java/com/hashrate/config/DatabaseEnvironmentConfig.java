package com.hashrate.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class DatabaseEnvironmentConfig {

    /**
     * Development environment specific configurations
     */
    @Configuration
    @Profile("dev")
    static class DevDatabaseConfig {
        
        @Bean
        public CommandLineRunner devDatabaseSetup(JdbcTemplate jdbcTemplate) {
            return args -> {
                System.out.println("🔧 Setting up development database configuration...");
                
                // Enable H2 Console
                System.out.println("H2 Console available at: http://localhost:8080/h2-console");
                System.out.println("JDBC URL: jdbc:h2:mem:hashrate_db");
                System.out.println("Username: sa | Password: password");
                
                // Create development-specific views
                createDevelopmentViews(jdbcTemplate);
                
                System.out.println("✅ Development database setup completed");
            };
        }
        
        private void createDevelopmentViews(JdbcTemplate jdbcTemplate) {
            String[] viewStatements = {
                """
                CREATE OR REPLACE VIEW v_active_products AS
                SELECT p.*, 
                       (SELECT COUNT(*) FROM product_features pf WHERE pf.product_id = p.id) as feature_count,
                       (SELECT COUNT(*) FROM product_specifications ps WHERE ps.product_id = p.id) as spec_count
                FROM products p 
                WHERE p.is_active = true
                ORDER BY p.display_order, p.name
                """,
                
                """
                CREATE OR REPLACE VIEW v_career_summary AS
                SELECT 
                    department,
                    COUNT(*) as total_positions,
                    COUNT(CASE WHEN is_urgent = true THEN 1 END) as urgent_positions,
                    COUNT(CASE WHEN expiry_date > CURRENT_TIMESTAMP THEN 1 END) as active_positions
                FROM careers 
                WHERE is_active = true 
                GROUP BY department
                """,
                
                """
                CREATE OR REPLACE VIEW v_solution_metrics AS
                SELECT 
                    type,
                    COUNT(*) as total_solutions,
                    COUNT(CASE WHEN is_featured = true THEN 1 END) as featured_solutions,
                    AVG(display_order) as avg_display_order
                FROM solutions 
                WHERE is_active = true 
                GROUP BY type
                """
            };
            
            for (String statement : viewStatements) {
                try {
                    jdbcTemplate.execute(statement);
                } catch (Exception e) {
                    System.err.println("Failed to create view: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Production environment specific configurations
     */
    @Configuration
    @Profile("prod")
    static class ProdDatabaseConfig {
        
        @Bean
        public CommandLineRunner prodDatabaseSetup(JdbcTemplate jdbcTemplate) {
            return args -> {
                System.out.println("🔧 Setting up production database configuration...");
                
                // Production-specific optimizations
                enableProductionOptimizations(jdbcTemplate);
                
                System.out.println("✅ Production database setup completed");
            };
        }
        
        private void enableProductionOptimizations(JdbcTemplate jdbcTemplate) {
            try {
                // Production specific settings would go here
                // For H2, we can set cache size and other performance parameters
                jdbcTemplate.execute("SET CACHE_SIZE 131072"); // 128MB cache
                System.out.println("✓ Production optimizations applied");
            } catch (Exception e) {
                System.err.println("Failed to apply production optimizations: " + e.getMessage());
            }
        }
    }
}