package Food_Orders.Controller;

import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import Food_Orders.Service.ExcelCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExcelCategoryService excelCategoryService;

    // Create a new category
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("imageUrl") String imageUrl) {
        try {
            Category category = new Category(name, description, imageUrl);
            Category savedCategory = categoryRepository.save(category);
            logger.info("Created new category with ID: {}", savedCategory.getCategory_id());
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            logger.error("Failed to create category", e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all categories
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        logger.info("Fetching all categories...");
        try {
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error fetching categories", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        logger.info("Fetching category with ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            logger.warn("Category with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("Attempting to delete category with ID: {}", id);
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            logger.info("Deleted category with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Category with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Import categories from Excel file
    @PostMapping("/import")
    public ResponseEntity<String> importCategories(@RequestParam("file") MultipartFile file) {
        try {
            // Process the file (this should call a service to process the Excel file)
            excelCategoryService.importCategoriesFromExcel(file);
            return ResponseEntity.ok("Categories imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to import categories: " + e.getMessage());
        }
    }

    // Export categories to Excel file
    @GetMapping("/export")
    public void exportCategories(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=categories.xlsx");
        excelCategoryService.exportCategoriesToExcel(response);
    }
}
