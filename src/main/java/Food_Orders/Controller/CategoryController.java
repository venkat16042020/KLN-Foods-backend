package Food_Orders.Controller;

import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

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
}
