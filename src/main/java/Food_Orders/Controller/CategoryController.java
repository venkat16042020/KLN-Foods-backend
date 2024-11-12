package Food_Orders.Controller;

import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import Food_Orders.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    private CategoryRepository categoryRepository;

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
    @PostMapping("/import")
    public ResponseEntity<?> uploadCategoryData(@RequestParam("file") MultipartFile file ) {
        this.categoryService.saveCategoriesToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message","Categories data uploaded and saved to database successfully"));
    }

    @GetMapping("/Export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Customers_Information.xlsx";
        response.setHeader(headerKey, headerValue);
        categoryService.exportCategoriesToExcel(response);
    }

}
