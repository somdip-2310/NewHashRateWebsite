package com.hashrate.controller;

import com.hashrate.model.Product;
import com.hashrate.model.Product.ProductCategory;
import com.hashrate.service.CareerService;
import com.hashrate.service.ProductService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	 @Autowired
	    public ProductController(ProductService productService) {
	        this.productService = productService;
	    }
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	

	@GetMapping
	public String index(Model model) {
		log.debug("Loading products page");

		List<Product> products = productService.findAllActive();
		List<ProductCategory> categories = productService.findAllCategories();

		model.addAttribute("products", products);
		model.addAttribute("categories", categories);

		// SEO
		model.addAttribute("pageTitle",
				"Products - Servers, Datacenter Hardware, Controllers | Hash Rate Communications");
		model.addAttribute("pageDescription",
				"Explore our range of enterprise servers, datacenter hardware, industrial controllers, and sensors from leading brands like HP, Dell, and Lenovo.");

		return "products/index";
	}

	@GetMapping("/servers")
	public String servers(Model model) {
		log.debug("Loading servers page");

		List<Product> servers = productService.findByCategory(ProductCategory.SERVERS_WORKSTATIONS);
		model.addAttribute("products", servers);

		// SEO
		model.addAttribute("pageTitle", "Servers & Workstations - HP, Dell, Lenovo | Hash Rate Communications");
		model.addAttribute("pageDescription",
				"High-performance servers and workstations from HP, Dell, and Lenovo. Customized solutions for your business needs with pan-India delivery.");

		return "products/servers";
	}

	@GetMapping("/datacenter-hardware")
	public String datacenterHardware(Model model) {
		log.debug("Loading datacenter hardware page");

		List<Product> hardware = productService.findByCategory(ProductCategory.DATACENTER_HARDWARE);
		model.addAttribute("products", hardware);

		// SEO
		model.addAttribute("pageTitle", "Datacenter Hardware - PDU, Smart Rack, KVM | Hash Rate Communications");
		model.addAttribute("pageDescription",
				"Complete datacenter hardware solutions including PDU, RDU, Smart Racks, KVM switches, ATS, and CRV systems for modern data centers.");

		return "products/datacenter-hardware";
	}

	@GetMapping("/controllers")
	public String controllers(Model model) {
		log.debug("Loading controllers page");

		List<Product> controllers = productService.findByCategory(ProductCategory.CONTROLLERS_CONVERTERS);
		model.addAttribute("products", controllers);

		// SEO
		model.addAttribute("pageTitle",
				"Industrial Controllers & Converters - PLC, RTU, DCS | Hash Rate Communications");
		model.addAttribute("pageDescription",
				"Industrial automation controllers including Siemens S7 Series, Allen-Bradley, Honeywell HC900, and various power converters.");

		return "products/controllers";
	}

	@GetMapping("/bms")
	public String bms(Model model) {
		log.debug("Loading BMS page");

		List<Product> sensors = productService.findByCategory(ProductCategory.INDUSTRIAL_SENSORS);
		model.addAttribute("products", sensors);

		// SEO
		model.addAttribute("pageTitle",
				"Building Management System (BMS) - Sensors, HVAC, Fire Safety | Hash Rate Communications");
		model.addAttribute("pageDescription",
				"Complete BMS solutions with industrial sensors, HVAC controls, and fire safety systems for intelligent building management.");

		return "products/bms";
	}

	@GetMapping("/{slug}")
	public String productDetail(@PathVariable String slug, Model model) {
		log.debug("Loading product detail for slug: {}", slug);

		Product product = productService.findBySlug(slug).orElseThrow(() -> new RuntimeException("Product not found"));

		model.addAttribute("product", product);

		// Add related products
		List<Product> relatedProducts = productService.findByCategory(product.getCategory()).stream()
				.filter(p -> !p.getId().equals(product.getId())).limit(4).toList();
		model.addAttribute("relatedProducts", relatedProducts);

		// SEO from product metadata
		if (product.getSeoMetadata() != null) {
			model.addAttribute("pageTitle", product.getSeoMetadata().getMetaTitle());
			model.addAttribute("pageDescription", product.getSeoMetadata().getMetaDescription());
			model.addAttribute("pageKeywords", product.getSeoMetadata().getMetaKeywords());
		}

		return "products/detail";
	}

	@GetMapping("/search")
	public String search(@RequestParam String q, @PageableDefault(size = 12) Pageable pageable, Model model) {
		log.debug("Searching products with query: {}", q);

		Page<Product> results = productService.searchProducts(q, pageable);

		model.addAttribute("products", results.getContent());
		model.addAttribute("page", results);
		model.addAttribute("query", q);

		// SEO
		model.addAttribute("pageTitle", "Search Results for '" + q + "' - Products | Hash Rate Communications");
		model.addAttribute("pageDescription", "Search results for '" + q
				+ "' in our product catalog. Find servers, datacenter hardware, controllers, and more.");

		return "products/search";
	}
}