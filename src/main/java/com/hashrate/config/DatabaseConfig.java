package com.hashrate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfig {

    @Value("${app.database.init-schema:true}")
    private boolean initSchema;
    
    @Value("${app.database.init-data:true}")
    private boolean initData;
    
    @Value("${app.database.create-indexes:true}")
    private boolean createIndexes;

    /**
     * Database schema initialization - Creates indexes and constraints
     */
    @Bean
    @Order(1)
    @ConditionalOnProperty(name = "app.database.init-schema", havingValue = "true", matchIfMissing = true)
    public CommandLineRunner initDatabaseSchema(JdbcTemplate jdbcTemplate) {
        return args -> {
            if (initSchema) {
                System.out.println("🔧 Initializing database schema and indexes...");
                createIndexes(jdbcTemplate);
                createConstraints(jdbcTemplate);
                createSequences(jdbcTemplate);
                System.out.println("✅ Database schema initialized successfully");
            }
        };
    }

    /**
     * Database health check and maintenance
     */
    @Bean
    @Order(2)
    @Profile("!test")
    public CommandLineRunner databaseHealthCheck(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("🏥 Performing database health check...");
            
            // Check database connectivity
            try {
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                System.out.println("✅ Database connectivity: OK");
            } catch (Exception e) {
                System.err.println("❌ Database connectivity failed: " + e.getMessage());
                throw e;
            }
            
            // Check table existence
            checkTableExistence(jdbcTemplate);
            
            // Performance optimization
            optimizeDatabase(jdbcTemplate);
            
            System.out.println("✅ Database health check completed");
        };
    }

    private void createIndexes(JdbcTemplate jdbcTemplate) {
        String[] indexStatements = {
            // Product indexes
            "CREATE INDEX IF NOT EXISTS idx_products_slug ON products(slug)",
            "CREATE INDEX IF NOT EXISTS idx_products_category ON products(category)",
            "CREATE INDEX IF NOT EXISTS idx_products_active ON products(is_active)",
            "CREATE INDEX IF NOT EXISTS idx_products_display_order ON products(display_order)",
            
            // Solution indexes
            "CREATE INDEX IF NOT EXISTS idx_solutions_slug ON solutions(slug)",
            "CREATE INDEX IF NOT EXISTS idx_solutions_type ON solutions(type)",
            "CREATE INDEX IF NOT EXISTS idx_solutions_active ON solutions(is_active)",
            "CREATE INDEX IF NOT EXISTS idx_solutions_featured ON solutions(is_featured)",
            
            // Career indexes
            "CREATE INDEX IF NOT EXISTS idx_careers_job_id ON careers(job_id)",
            "CREATE INDEX IF NOT EXISTS idx_careers_department ON careers(department)",
            "CREATE INDEX IF NOT EXISTS idx_careers_location ON careers(location)",
            "CREATE INDEX IF NOT EXISTS idx_careers_active ON careers(is_active)",
            "CREATE INDEX IF NOT EXISTS idx_careers_urgent ON careers(is_urgent)",
            "CREATE INDEX IF NOT EXISTS idx_careers_expiry ON careers(expiry_date)",
            
            // Service indexes
            "CREATE INDEX IF NOT EXISTS idx_services_slug ON services(slug)",
            "CREATE INDEX IF NOT EXISTS idx_services_category ON services(category)",
            "CREATE INDEX IF NOT EXISTS idx_services_active ON services(is_active)",
            
            // Project indexes
            "CREATE INDEX IF NOT EXISTS idx_projects_category ON projects(category)",
            "CREATE INDEX IF NOT EXISTS idx_projects_active ON projects(is_active)",
            "CREATE INDEX IF NOT EXISTS idx_projects_featured ON projects(is_featured)",
            "CREATE INDEX IF NOT EXISTS idx_projects_client ON projects(client_name)",
            
            // Composite indexes for common queries
            "CREATE INDEX IF NOT EXISTS idx_products_active_category ON products(is_active, category)",
            "CREATE INDEX IF NOT EXISTS idx_solutions_active_featured ON solutions(is_active, is_featured)",
            "CREATE INDEX IF NOT EXISTS idx_careers_active_expiry ON careers(is_active, expiry_date)"
        };
        
        for (String statement : indexStatements) {
            try {
                jdbcTemplate.execute(statement);
            } catch (Exception e) {
                System.err.println("Failed to create index: " + statement + " - " + e.getMessage());
            }
        }
    }

    private void createConstraints(JdbcTemplate jdbcTemplate) {
        String[] constraintStatements = {
            // Ensure display_order is positive
            "ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_display_order CHECK (display_order >= 0)",
            "ALTER TABLE solutions ADD CONSTRAINT IF NOT EXISTS chk_solutions_display_order CHECK (display_order >= 0)",
            "ALTER TABLE careers ADD CONSTRAINT IF NOT EXISTS chk_careers_display_order CHECK (display_order >= 0)",
            
            // Ensure future expiry dates for careers
            "ALTER TABLE careers ADD CONSTRAINT IF NOT EXISTS chk_careers_expiry_future CHECK (expiry_date > created_at)"
        };
        
        for (String statement : constraintStatements) {
            try {
                jdbcTemplate.execute(statement);
            } catch (Exception e) {
                System.err.println("Failed to create constraint: " + statement + " - " + e.getMessage());
            }
        }
    }

    private void createSequences(JdbcTemplate jdbcTemplate) {
        String[] sequenceStatements = {
            "CREATE SEQUENCE IF NOT EXISTS job_id_seq START WITH 1000 INCREMENT BY 1",
            "CREATE SEQUENCE IF NOT EXISTS project_ref_seq START WITH 100 INCREMENT BY 1"
        };
        
        for (String statement : sequenceStatements) {
            try {
                jdbcTemplate.execute(statement);
            } catch (Exception e) {
                System.err.println("Failed to create sequence: " + statement + " - " + e.getMessage());
            }
        }
    }

    private void checkTableExistence(JdbcTemplate jdbcTemplate) {
        String[] requiredTables = {
            "products", "solutions", "careers", "services", "projects",
            "product_features", "product_specifications",
            "solution_features", "solution_benefits", "solution_use_cases",
            "career_requirements", "career_responsibilities",
            "service_deliverables", "service_technologies",
            "project_services"
        };
        
        for (String table : requiredTables) {
            try {
                jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = UPPER(?)", 
                    Integer.class, table
                );
                System.out.println("✓ Table exists: " + table);
            } catch (Exception e) {
                System.err.println("❌ Missing table: " + table);
            }
        }
    }

    private void optimizeDatabase(JdbcTemplate jdbcTemplate) {
        try {
            // Update table statistics for H2
            jdbcTemplate.execute("ANALYZE");
            System.out.println("✓ Database statistics updated");
        } catch (Exception e) {
            System.err.println("Failed to optimize database: " + e.getMessage());
        }
    }
}