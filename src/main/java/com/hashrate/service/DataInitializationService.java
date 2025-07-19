package com.hashrate.service;

import com.hashrate.model.*;
import com.hashrate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Order(10) // Execute after database schema initialization
public class DataInitializationService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationService.class);

    @Value("${app.data.force-reload:false}")
    private boolean forceReload;
    
    @Value("${app.data.sample-size:10}")
    private int sampleSize;

    @Autowired private ProductRepository productRepository;
    @Autowired private SolutionRepository solutionRepository;
    @Autowired private CareerRepository careerRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private ProjectRepository projectRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("🚀 Starting comprehensive data initialization...");
        
        try {
            if (forceReload) {
                log.warn("⚠️ Force reload enabled - clearing existing data");
                clearAllData();
            }
            
            // Initialize data in dependency order
            initializeProducts();
            initializeSolutions();
            initializeServices();
            initializeCareers();
            initializeProjects();
            
            // Generate summary report
            generateInitializationReport();
            
            log.info("✅ Data initialization completed successfully");
            
        } catch (Exception e) {
            log.error("❌ Data initialization failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void clearAllData() {
        log.info("🗑️ Clearing existing data...");
        
        // Clear in reverse dependency order
        projectRepository.deleteAll();
        careerRepository.deleteAll();
        serviceRepository.deleteAll();
        solutionRepository.deleteAll();
        productRepository.deleteAll();
        
        log.info("✅ Data cleared successfully");
    }

    @Transactional
    public void initializeProducts() {
        if (productRepository.count() > 0 && !forceReload) {
            log.info("⏭️ Products already exist, skipping initialization");
            return;
        }
        
        log.info("📦 Initializing products...");
        
        List<ProductData> productDataList = List.of(
            new ProductData("enterprise-server-rack-42u", "Enterprise Server Rack 42U", 
                "High-density 42U server rack designed for enterprise data centers with advanced cooling and cable management",
                "Enterprise-grade 42U server rack with premium features",
                Product.ProductCategory.SERVERS_WORKSTATIONS,
                "/images/products/server-rack-42u.jpg", "/images/products/thumbnails/server-rack-42u-thumb.jpg",
                Arrays.asList(
                    "42U rack height with adjustable mounting rails",
                    "Advanced ventilation system with front-to-back airflow",
                    "Integrated cable management with vertical organizers",
                    "Lockable front and rear doors with perforated design",
                    "Heavy-duty casters for easy positioning",
                    "Tool-free installation for most equipment"
                ),
                Arrays.asList(
                    "Dimensions: 2000mm (H) x 600mm (W) x 1200mm (D)",
                    "Load capacity: 1500kg static, 800kg dynamic",
                    "Material: Cold-rolled steel with powder coating",
                    "Perforation: 68% front door, 85% rear door",
                    "Color: RAL 9005 (Jet Black) standard",
                    "Compliance: EIA-310-D, IEC 60297, ANSI/TIA-942"
                ), 1),
                
            new ProductData("datacenter-cooling-unit-precision", "Precision Datacenter Cooling Unit",
                "Advanced precision cooling system designed for high-density computing environments with intelligent temperature control",
                "Precision cooling for critical datacenter infrastructure",
                Product.ProductCategory.DATACENTER_HARDWARE,
                "/images/products/cooling-unit-precision.jpg", "/images/products/thumbnails/cooling-unit-precision-thumb.jpg",
                Arrays.asList(
                    "Variable speed EC fans with intelligent control",
                    "Hot aisle/cold aisle containment compatibility",
                    "Redundant compressors for high availability",
                    "Advanced filtration system with washable filters",
                    "Remote monitoring and alerting capabilities",
                    "Energy-efficient operation with EER > 3.0"
                ),
                Arrays.asList(
                    "Cooling capacity: 20-100 kW (scalable)",
                    "Operating temperature range: 15-35°C",
                    "Humidity control: 40-60% RH ±5%",
                    "Power supply: 380-415V, 50/60Hz, 3-phase",
                    "Refrigerant: R134a or R1234ze (eco-friendly)",
                    "Control system: PLC-based with HMI touchscreen"
                ), 2),
                
            new ProductData("industrial-sensor-temperature-wireless", "Wireless Temperature Sensor",
                "Industrial-grade wireless temperature sensor with long-range communication and battery backup",
                "Wireless temperature monitoring for industrial applications",
                Product.ProductCategory.INDUSTRIAL_SENSORS,
                "/images/products/temp-sensor-wireless.jpg", "/images/products/thumbnails/temp-sensor-wireless-thumb.jpg",
                Arrays.asList(
                    "Wireless communication up to 1km range",
                    "IP67 weatherproof enclosure",
                    "10-year battery life with lithium primary cells",
                    "Real-time alerts and threshold monitoring",
                    "Easy installation with magnetic mounting",
                    "Integration with SCADA and BMS systems"
                ),
                Arrays.asList(
                    "Temperature range: -40°C to +85°C",
                    "Accuracy: ±0.1°C typical, ±0.2°C maximum",
                    "Communication: LoRaWAN 1.0.3 compatible",
                    "Update interval: 1 minute to 24 hours (configurable)",
                    "Enclosure: IP67 rated polycarbonate",
                    "Certification: CE, FCC, IC, ACMA"
                ), 3),
                
            new ProductData("plc-controller-industrial-modular", "Modular Industrial PLC Controller",
                "High-performance modular PLC system for complex automation and control applications",
                "Advanced PLC system for industrial automation",
                Product.ProductCategory.CONTROLLERS_CONVERTERS,
                "/images/products/plc-modular.jpg", "/images/products/thumbnails/plc-modular-thumb.jpg",
                Arrays.asList(
                    "Modular design with hot-swappable I/O modules",
                    "Dual Ethernet ports with ring topology support",
                    "Built-in web server for remote monitoring",
                    "IEC 61131-3 compliant programming",
                    "Redundant power supply options",
                    "Real-time clock with battery backup"
                ),
                Arrays.asList(
                    "CPU: 32-bit ARM Cortex-A9, 1GHz",
                    "Memory: 1GB RAM, 4GB Flash storage",
                    "I/O capacity: Up to 8192 digital, 1024 analog points",
                    "Communication: Ethernet, RS485, CAN, Profibus",
                    "Operating temperature: -25°C to +60°C",
                    "Certifications: UL, CE, ATEX, IECEx"
                ), 4),
                
            new ProductData("cloud-storage-enterprise-nas", "Enterprise NAS Cloud Storage",
                "High-capacity Network Attached Storage system with cloud integration and enterprise security",
                "Enterprise-grade NAS with cloud capabilities",
                Product.ProductCategory.CLOUD_STORAGE,
                "/images/products/nas-enterprise.jpg", "/images/products/thumbnails/nas-enterprise-thumb.jpg",
                Arrays.asList(
                    "Hybrid cloud integration with major providers",
                    "Hardware RAID with hot-swappable drives",
                    "Advanced data deduplication and compression",
                    "Enterprise-grade security with encryption",
                    "Automated backup and disaster recovery",
                    "10GbE network connectivity standard"
                ),
                Arrays.asList(
                    "Capacity: 4TB to 192TB (expandable)",
                    "Drive bays: 8-24 hot-swappable 3.5\" SATA/SAS",
                    "RAID levels: 0, 1, 5, 6, 10, 50, 60",
                    "Network: Dual 10GbE ports, 4x 1GbE ports",
                    "Processor: Intel Xeon D-series",
                    "RAM: 16GB DDR4 ECC (expandable to 128GB)"
                ), 5)
        );
        
        AtomicInteger counter = new AtomicInteger(0);
        productDataList.forEach(data -> {
            try {
                Product product = createProduct(data);
                productRepository.save(product);
                counter.incrementAndGet();
                log.debug("✓ Created product: {}", data.name);
            } catch (Exception e) {
                log.error("❌ Failed to create product: {} - {}", data.name, e.getMessage());
            }
        });
        
        log.info("✅ Initialized {} products", counter.get());
    }

    @Transactional
    public void initializeSolutions() {
        if (solutionRepository.count() > 0 && !forceReload) {
            log.info("⏭️ Solutions already exist, skipping initialization");
            return;
        }
        
        log.info("💡 Initializing solutions...");
        
        List<SolutionData> solutionDataList = List.of(
            new SolutionData("comprehensive-dcim-platform", "Comprehensive DCIM Platform",
                "Complete Data Center Infrastructure Management solution providing real-time monitoring, capacity planning, and energy optimization for modern data centers",
                "End-to-end DCIM solution for optimal data center operations",
                Solution.SolutionType.DCIM, "fas fa-server",
                "/images/solutions/dcim-comprehensive.jpg", "/images/solutions/banners/dcim-banner.jpg",
                Arrays.asList(
                    "Real-time monitoring of power, cooling, and space",
                    "Predictive analytics for capacity planning",
                    "3D visualization of data center assets",
                    "Automated alerting and notification system",
                    "Energy efficiency optimization algorithms",
                    "Integration with existing ITSM and monitoring tools"
                ),
                Arrays.asList(
                    "Reduced operational costs by up to 30%",
                    "Improved energy efficiency and PUE optimization",
                    "Enhanced uptime through predictive maintenance",
                    "Streamlined compliance reporting",
                    "Better resource utilization and planning",
                    "Reduced risk of outages and downtime"
                ),
                Arrays.asList(
                    "Enterprise data centers with 100+ racks",
                    "Colocation facilities requiring tenant management",
                    "Hyperscale cloud infrastructure deployments",
                    "Government and military facilities",
                    "Financial services critical infrastructure"
                ),
                "Multi-tier architecture with redundant data collection, centralized analytics engine, and distributed monitoring agents",
                true, 1),
                
            new SolutionData("intelligent-asset-tracking", "Intelligent Asset Tracking System",
                "RFID and IoT-based asset tracking solution providing real-time location, status monitoring, and lifecycle management for critical infrastructure assets",
                "Smart asset tracking with IoT integration",
                Solution.SolutionType.ASSET_TRACKING, "fas fa-map-marker-alt",
                "/images/solutions/asset-tracking.jpg", "/images/solutions/banners/asset-tracking-banner.jpg",
                Arrays.asList(
                    "RFID and barcode scanning capabilities",
                    "Real-time location tracking with indoor positioning",
                    "Automated asset lifecycle management",
                    "Mobile applications for field technicians",
                    "Integration with CMMS and ERP systems",
                    "Custom reporting and analytics dashboard"
                ),
                Arrays.asList(
                    "99% asset visibility and accountability",
                    "Reduced asset search time by 75%",
                    "Improved maintenance scheduling efficiency",
                    "Enhanced security and theft prevention",
                    "Automated compliance documentation",
                    "Lower total cost of ownership"
                ),
                Arrays.asList(
                    "Manufacturing facilities with mobile equipment",
                    "Healthcare institutions tracking medical devices",
                    "Construction sites managing tools and materials",
                    "Warehouses and distribution centers",
                    "Educational institutions managing IT assets"
                ),
                "Cloud-native microservices architecture with edge computing capabilities for offline operation",
                true, 2),
                
            new SolutionData("advanced-chiller-plant-management", "Advanced Chiller Plant Management",
                "Intelligent chiller plant optimization system using AI/ML algorithms to maximize efficiency while maintaining optimal cooling performance",
                "AI-powered chiller plant optimization",
                Solution.SolutionType.CHILLER_PLANT_MANAGEMENT, "fas fa-snowflake",
                "/images/solutions/chiller-management.jpg", "/images/solutions/banners/chiller-banner.jpg",
                Arrays.asList(
                    "AI-driven optimization algorithms",
                    "Real-time performance monitoring",
                    "Predictive maintenance scheduling",
                    "Energy consumption optimization",
                    "Remote monitoring and control",
                    "Historical trending and analysis"
                ),
                Arrays.asList(
                    "20-30% reduction in energy consumption",
                    "Extended equipment lifespan",
                    "Improved system reliability",
                    "Reduced maintenance costs",
                    "Better environmental compliance",
                    "Enhanced operational visibility"
                ),
                Arrays.asList(
                    "Large commercial buildings and complexes",
                    "Industrial manufacturing facilities",
                    "Data centers requiring precise cooling",
                    "Hospitals and healthcare facilities",
                    "Educational campuses"
                ),
                "Distributed control system with machine learning backend and real-time optimization engine",
                false, 3)
        );
        
        AtomicInteger counter = new AtomicInteger(0);
        solutionDataList.forEach(data -> {
            try {
                Solution solution = createSolution(data);
                solutionRepository.save(solution);
                counter.incrementAndGet();
                log.debug("✓ Created solution: {}", data.title);
            } catch (Exception e) {
                log.error("❌ Failed to create solution: {} - {}", data.title, e.getMessage());
            }
        });
        
        log.info("✅ Initialized {} solutions", counter.get());
    }

    @Transactional
    public void initializeCareers() {
        if (careerRepository.count() > 0 && !forceReload) {
            log.info("⏭️ Careers already exist, skipping initialization");
            return;
        }
        
        log.info("👥 Initializing careers...");
        
        List<CareerData> careerDataList = List.of(
            new CareerData("HRC-2025-001", "Senior Java Developer",
                "Join our engineering team to develop cutting-edge enterprise applications for data center management and industrial automation. Work with the latest technologies in a collaborative environment.",
                Career.Department.ENGINEERING, "Bangalore, India",
                "Senior", "Full-time", "15-22 LPA",
                Arrays.asList(
                    "5+ years of Java development experience",
                    "Strong knowledge of Spring Boot and microservices",
                    "Experience with RESTful APIs and database design",
                    "Familiarity with cloud platforms (AWS/Azure)",
                    "Knowledge of DevOps practices and CI/CD",
                    "Bachelor's degree in Computer Science or related field"
                ),
                Arrays.asList(
                    "Design and develop scalable Java applications",
                    "Collaborate with cross-functional teams",
                    "Write clean, maintainable, and efficient code",
                    "Participate in code reviews and technical discussions",
                    "Mentor junior developers and share best practices",
                    "Contribute to architectural decisions"
                ), false, 1),
                
            new CareerData("HRC-2025-002", "DevOps Engineer",
                "Lead our infrastructure automation initiatives and help scale our cloud-native applications. Work with modern DevOps tools and practices in a fast-paced environment.",
                Career.Department.ENGINEERING, "Mumbai, India",
                "Senior", "Full-time", "18-25 LPA",
                Arrays.asList(
                    "4+ years of DevOps/SRE experience",
                    "Expertise in AWS/Azure cloud platforms",
                    "Strong knowledge of Docker and Kubernetes",
                    "Experience with Infrastructure as Code (Terraform)",
                    "Proficiency in CI/CD tools (Jenkins, GitLab CI)",
                    "Scripting skills in Python, Bash, or PowerShell"
                ),
                Arrays.asList(
                    "Design and implement CI/CD pipelines",
                    "Manage cloud infrastructure and deployments",
                    "Monitor system performance and reliability",
                    "Automate repetitive operational tasks",
                    "Collaborate with development teams on best practices",
                    "Implement security and compliance measures"
                ), true, 2),
                
            new CareerData("HRC-2025-003", "Sales Manager - Industrial Solutions",
                "Drive revenue growth by selling our DCIM and industrial automation solutions to enterprise clients. Build relationships with key decision makers and manage the complete sales cycle.",
                Career.Department.SALES, "Delhi NCR, India",
                "Senior", "Full-time", "12-18 LPA + Commission",
                Arrays.asList(
                    "5+ years of B2B sales experience",
                    "Experience selling technical/industrial solutions",
                    "Strong network in data center or industrial sectors",
                    "Excellent communication and presentation skills",
                    "Proven track record of meeting sales targets",
                    "MBA or equivalent qualification preferred"
                ),
                Arrays.asList(
                    "Identify and pursue new business opportunities",
                    "Manage the complete sales cycle from lead to closure",
                    "Build and maintain relationships with key clients",
                    "Collaborate with technical teams for solution design",
                    "Prepare proposals and contract negotiations",
                    "Achieve quarterly and annual sales targets"
                ), false, 3),
                
            new CareerData("HRC-2025-004", "Project Manager - Data Center",
                "Lead complex data center implementation projects from planning to deployment. Coordinate with multiple stakeholders and ensure successful project delivery within scope, time, and budget.",
                Career.Department.OPERATIONS, "Chennai, India",
                "Senior", "Full-time", "14-20 LPA",
                Arrays.asList(
                    "PMP or equivalent project management certification",
                    "5+ years of data center project management experience",
                    "Strong knowledge of DCIM and BMS systems",
                    "Experience with Agile and Waterfall methodologies",
                    "Excellent stakeholder management skills",
                    "Engineering background preferred"
                ),
                Arrays.asList(
                    "Plan and execute data center infrastructure projects",
                    "Coordinate with internal teams and external vendors",
                    "Manage project timelines, budgets, and resources",
                    "Conduct risk assessment and mitigation planning",
                    "Ensure quality deliverables and client satisfaction",
                    "Prepare project reports and documentation"
                ), true, 4),
                
            new CareerData("HRC-2025-005", "Marketing Specialist - Digital",
                "Drive our digital marketing initiatives and help build our brand presence in the data center and industrial automation markets. Create compelling content and manage multi-channel campaigns.",
                Career.Department.MARKETING, "Pune, India",
                "Intermediate", "Full-time", "8-12 LPA",
                Arrays.asList(
                    "3+ years of digital marketing experience",
                    "Experience with B2B marketing campaigns",
                    "Proficiency in marketing automation tools",
                    "Strong content creation and copywriting skills",
                    "Knowledge of SEO/SEM and social media marketing",
                    "Analytics skills with Google Analytics/similar tools"
                ),
                Arrays.asList(
                    "Develop and execute digital marketing strategies",
                    "Create compelling content for various channels",
                    "Manage social media presence and campaigns",
                    "Analyze campaign performance and optimize ROI",
                    "Coordinate with sales team for lead generation",
                    "Support trade shows and industry events"
                ), false, 5)
        );
        
        AtomicInteger counter = new AtomicInteger(0);
        careerDataList.forEach(data -> {
            try {
                Career career = createCareer(data);
                careerRepository.save(career);
                counter.incrementAndGet();
                log.debug("✓ Created career: {}", data.title);
            } catch (Exception e) {
                log.error("❌ Failed to create career: {} - {}", data.title, e.getMessage());
            }
        });
        
        log.info("✅ Initialized {} career positions", counter.get());
    }

    @Transactional
    public void initializeServices() {
        if (serviceRepository.count() > 0 && !forceReload) {
            log.info("⏭️ Services already exist, skipping initialization");
            return;
        }
        
        log.info("🔧 Initializing services...");
        
        List<ServiceData> serviceDataList = List.of(
            new ServiceData("project-management-turnkey", "Turnkey Project Management",
                "End-to-end project management services for data center and industrial infrastructure projects, from initial planning to final commissioning and handover",
                "Complete project lifecycle management",
                com.hashrate.model.Service.ServiceCategory.PROJECT_MANAGEMENT, "fas fa-project-diagram",
                "/images/services/project-management.jpg",
                Arrays.asList(
                    "Project planning and scheduling",
                    "Resource allocation and management",
                    "Vendor coordination and supervision",
                    "Quality assurance and testing",
                    "Documentation and compliance",
                    "Training and knowledge transfer"
                ),
                Arrays.asList(
                    "Microsoft Project, Primavera P6",
                    "Agile and Waterfall methodologies",
                    "Risk management frameworks",
                    "Quality management systems",
                    "Collaboration platforms",
                    "Reporting and analytics tools"
                ), true, 1),
                
            new ServiceData("bms-operations-support", "BMS Operations & Support",
                "Comprehensive Building Management System operations including 24/7 monitoring, maintenance, optimization, and emergency response services",
                "Complete BMS operational support",
                com.hashrate.model.Service.ServiceCategory.BMS_OPERATIONS, "fas fa-building",
                "/images/services/bms-operations.jpg",
                Arrays.asList(
                    "24/7 monitoring and control",
                    "Preventive maintenance scheduling",
                    "Emergency response services",
                    "Energy optimization programs",
                    "System upgrades and enhancements",
                    "Compliance reporting and documentation"
                ),
                Arrays.asList(
                    "Schneider Electric EcoStruxure",
                    "Honeywell Building Suite",
                    "Johnson Controls Metasys",
                    "Siemens Desigo CC",
                    "Remote monitoring platforms",
                    "Mobile maintenance applications"
                ), true, 2),
                
            new ServiceData("dcim-implementation", "DCIM Implementation Services",
                "Complete Data Center Infrastructure Management implementation including system design, installation, configuration, and staff training",
                "Professional DCIM deployment services",
                com.hashrate.model.Service.ServiceCategory.DCIM, "fas fa-server",
                "/images/services/dcim-implementation.jpg",
                Arrays.asList(
                    "System architecture design",
                    "Hardware and software installation",
                    "Configuration and customization",
                    "Data migration and integration",
                    "User training and certification",
                    "Ongoing support and maintenance"
                ),
                Arrays.asList(
                    "Schneider Electric StruxureWare",
                    "Vertiv Trellis Platform",
                    "Nlyte DCIM Suite",
                    "Sunbird dcTrack",
                    "Custom integration APIs",
                    "Cloud-based monitoring"
                ), false, 3)
        );
        
        AtomicInteger counter = new AtomicInteger(0);
        serviceDataList.forEach(data -> {
            try {
                com.hashrate.model.Service service = createService(data);
                serviceRepository.save(service);
                counter.incrementAndGet();
                log.debug("✓ Created service: {}", data.name);
            } catch (Exception e) {
                log.error("❌ Failed to create service: {} - {}", data.name, e.getMessage());
            }
        });
        
        log.info("✅ Initialized {} services", counter.get());
    }

    @Transactional
    public void initializeProjects() {
        if (projectRepository.count() > 0 && !forceReload) {
            log.info("⏭️ Projects already exist, skipping initialization");
            return;
        }
        
        log.info("🏗️ Initializing projects...");
        
        List<ProjectData> projectDataList = List.of(
            new ProjectData("Bangalore Metro Rail Corporation", "/images/clients/bmrc-logo.png",
                "Metro Rail DCIM Implementation",
                "Comprehensive Data Center Infrastructure Management implementation for Bangalore Metro's central control facility, including real-time monitoring of critical systems",
                Project.ProjectCategory.AIRPORT_METRO, "Bangalore, Karnataka",
                Arrays.asList("DCIM Implementation", "24/7 Monitoring", "Energy Management", "Compliance Reporting"),
                "18 months", LocalDateTime.now().minusMonths(6), true, 1),
                
            new ProjectData("State Bank of India", "/images/clients/sbi-logo.png",
                "Branch Network BMS Upgrade",
                "Building Management System upgrade across 500+ SBI branches nationwide, including HVAC control, security integration, and energy optimization",
                Project.ProjectCategory.BFSI, "Pan India",
                Arrays.asList("BMS Upgrade", "HVAC Automation", "Security Integration", "Energy Optimization"),
                "24 months", LocalDateTime.now().minusMonths(3), true, 2),
                
            new ProjectData("Reliance Industries Limited", "/images/clients/ril-logo.png",
                "Petrochemical Plant Automation",
                "Industrial automation and monitoring system implementation for petrochemical manufacturing facility with advanced PLC and SCADA integration",
                Project.ProjectCategory.OIL_GAS_MFG, "Jamnagar, Gujarat",
                Arrays.asList("PLC Programming", "SCADA Development", "Safety Systems", "Process Optimization"),
                "30 months", LocalDateTime.now().minusMonths(1), false, 3),
                
            new ProjectData("NTT Communications", "/images/clients/ntt-logo.png",
                "Hyperscale Data Center DCIM",
                "Next-generation DCIM solution for 10MW hyperscale data center facility with AI-powered analytics and predictive maintenance capabilities",
                Project.ProjectCategory.DATACENTER, "Mumbai, Maharashtra",
                Arrays.asList("DCIM Platform", "AI Analytics", "Predictive Maintenance", "Capacity Planning"),
                "15 months", LocalDateTime.now().plusMonths(2), true, 4),
                
            new ProjectData("Government of Kerala", "/images/clients/kerala-govt-logo.png",
                "Smart City Infrastructure",
                "Integrated smart city infrastructure management system for Kochi Smart City project including traffic management, utilities monitoring, and citizen services",
                Project.ProjectCategory.GOVERNMENT, "Kochi, Kerala",
                Arrays.asList("Smart Infrastructure", "Traffic Management", "Utilities Monitoring", "Citizen Portal"),
                "36 months", LocalDateTime.now().plusMonths(8), false, 5)
        );
        
        AtomicInteger counter = new AtomicInteger(0);
        projectDataList.forEach(data -> {
            try {
                Project project = createProject(data);
                projectRepository.save(project);
                counter.incrementAndGet();
                log.debug("✓ Created project: {}", data.projectName);
            } catch (Exception e) {
                log.error("❌ Failed to create project: {} - {}", data.projectName, e.getMessage());
            }
        });
        
        log.info("✅ Initialized {} projects", counter.get());
    }

    private void generateInitializationReport() {
        log.info("\n" + "=".repeat(50));
        log.info("📊 DATA INITIALIZATION SUMMARY REPORT");
        log.info("=".repeat(50));
        log.info("Products initialized: {}", productRepository.count());
        log.info("Solutions initialized: {}", solutionRepository.count());
        log.info("Services initialized: {}", serviceRepository.count());
        log.info("Career positions: {}", careerRepository.count());
        log.info("Projects initialized: {}", projectRepository.count());
        log.info("Total records: {}", 
            productRepository.count() + solutionRepository.count() + 
            serviceRepository.count() + careerRepository.count() + projectRepository.count());
        log.info("=".repeat(50));
    }

    // Helper methods to create entities
    private Product createProduct(ProductData data) {
        Product product = new Product();
        product.setSlug(data.slug);
        product.setName(data.name);
        product.setDescription(data.description);
        product.setShortDescription(data.shortDescription);
        product.setCategory(data.category);
        product.setImageUrl(data.imageUrl);
        product.setThumbnailUrl(data.thumbnailUrl);
        product.setFeatures(data.features);
        product.setSpecifications(data.specifications);
        product.setActive(true);
        product.setDisplayOrder(data.displayOrder);
        
        // Generate SEO metadata
        SeoMetadata seo = new SeoMetadata();
        seo.setMetaTitle(data.name + " - Hash Rate Communications");
        seo.setMetaDescription(data.shortDescription);
        seo.setOgTitle(data.name);
        seo.setOgDescription(data.shortDescription);
        seo.setOgImage(data.imageUrl);
        product.setSeoMetadata(seo);
        
        return product;
    }

    private Solution createSolution(SolutionData data) {
        Solution solution = new Solution();
        solution.setSlug(data.slug);
        solution.setTitle(data.title);
        solution.setDescription(data.description);
        solution.setShortDescription(data.shortDescription);
        solution.setType(data.type);
        solution.setIconClass(data.iconClass);
        solution.setImageUrl(data.imageUrl);
        solution.setBannerImageUrl(data.bannerImageUrl);
        solution.setFeatures(data.features);
        solution.setBenefits(data.benefits);
        solution.setUseCases(data.useCases);
        solution.setArchitectureDetails(data.architectureDetails);
        solution.setActive(true);
        solution.setFeatured(data.featured);
        solution.setDisplayOrder(data.displayOrder);
        
        // Generate SEO metadata
        SeoMetadata seo = new SeoMetadata();
        seo.setMetaTitle(data.title + " - Hash Rate Communications");
        seo.setMetaDescription(data.shortDescription);
        seo.setOgTitle(data.title);
        seo.setOgDescription(data.shortDescription);
        seo.setOgImage(data.imageUrl);
        solution.setSeoMetadata(seo);
        
        return solution;
    }

    private Career createCareer(CareerData data) {
        Career career = new Career();
        career.setJobId(data.jobId);
        career.setTitle(data.title);
        career.setDescription(data.description);
        career.setDepartment(data.department);
        career.setLocation(data.location);
        career.setExperienceLevel(data.experienceLevel);
        career.setJobType(data.jobType);
        career.setSalaryRange(data.salaryRange);
        career.setRequirements(data.requirements);
        career.setResponsibilities(data.responsibilities);
        career.setActive(true);
        career.setUrgent(data.urgent);
        career.setExpiryDate(LocalDateTime.now().plusMonths(6)); // 6 months from now
        career.setDisplayOrder(data.displayOrder);
        
        return career;
    }

    private com.hashrate.model.Service createService(ServiceData data) {
        com.hashrate.model.Service service = new com.hashrate.model.Service();
        service.setSlug(data.slug);
        service.setName(data.name);
        service.setDescription(data.description);
        service.setShortDescription(data.shortDescription);
        service.setCategory(data.category);
        service.setIconClass(data.iconClass);
        service.setImageUrl(data.imageUrl);
        service.setDeliverables(data.deliverables);
        service.setTechnologies(data.technologies);
        service.setActive(true);
        service.setFeatured(data.featured);
        service.setDisplayOrder(data.displayOrder);
        
        return service;
    }

    private Project createProject(ProjectData data) {
        Project project = new Project();
        project.setClientName(data.clientName);
        project.setClientLogoUrl(data.clientLogoUrl);
        project.setProjectName(data.projectName);
        project.setDescription(data.description);
        project.setCategory(data.category);
        project.setLocation(data.location);
        project.setServicesProvided(data.servicesProvided);
        project.setProjectDuration(data.projectDuration);
        project.setCompletionDate(data.completionDate);
        project.setActive(true);
        project.setFeatured(data.featured);
        project.setDisplayOrder(data.displayOrder);
        
        return project;
    }

    // Data classes for initialization
    private record ProductData(String slug, String name, String description, String shortDescription,
                              Product.ProductCategory category, String imageUrl, String thumbnailUrl,
                              List<String> features, List<String> specifications, int displayOrder) {}

    private record SolutionData(String slug, String title, String description, String shortDescription,
                               Solution.SolutionType type, String iconClass, String imageUrl, String bannerImageUrl,
                               List<String> features, List<String> benefits, List<String> useCases,
                               String architectureDetails, boolean featured, int displayOrder) {}

    private record CareerData(String jobId, String title, String description, Career.Department department,
                             String location, String experienceLevel, String jobType, String salaryRange,
                             List<String> requirements, List<String> responsibilities, boolean urgent, int displayOrder) {}

    private record ServiceData(String slug, String name, String description, String shortDescription,
                              com.hashrate.model.Service.ServiceCategory category, String iconClass, String imageUrl,
                              List<String> deliverables, List<String> technologies, boolean featured, int displayOrder) {}

    private record ProjectData(String clientName, String clientLogoUrl, String projectName, String description,
                              Project.ProjectCategory category, String location, List<String> servicesProvided,
                              String projectDuration, LocalDateTime completionDate, boolean featured, int displayOrder) {}
}