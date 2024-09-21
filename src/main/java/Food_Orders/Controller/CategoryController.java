package Food_Orders.Controller;



import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    private static final String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("imageUrl") String imageUrl) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setImageUrl(imageUrl);

            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws Exception {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(name);
            category.setDescription(description);

            if (imageFile != null && !imageFile.isEmpty()) {
                // If a new image is provided, save it
                String imagePath = saveImage(imageFile);
                category.setImageUrl(imagePath);
            }

            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    private String saveImage(MultipartFile imageFile) throws Exception {

        Path imagePath = Paths.get(uploadDirectory);
        if (!Files.exists(imagePath)) {
            Files.createDirectories(imagePath);
        }
        // Save the file to the directory
        String fileName = imageFile.getOriginalFilename();
        Path filePath = imagePath.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());
        return filePath.toString();
    }
}

