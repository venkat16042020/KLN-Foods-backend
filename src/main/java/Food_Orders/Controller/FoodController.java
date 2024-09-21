package Food_Orders.Controller;

import Food_Orders.Entity.Food;
import Food_Orders.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foods")
@CrossOrigin(origins = "http://localhost:3000")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    // Create a new food item
    @PostMapping("/create")
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        try {
            Food savedFood = foodRepository.save(food);
            return ResponseEntity.ok(savedFood);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get all food items
    @GetMapping("/all")
    public ResponseEntity<List<Food>> getAllFoods() {
        try {
            List<Food> foods = foodRepository.findAll();
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get a food item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFoodById(@PathVariable Long id) {
        try {
            Optional<Food> food = foodRepository.findById(id);
            if (food.isPresent()) {
                return ResponseEntity.ok(food.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item with ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving food item: " + e.getMessage());
        }
    }

    // Update an existing food item
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody Food updatedFood) {
        try {
            Optional<Food> foodOptional = foodRepository.findById(id);
            if (foodOptional.isPresent()) {
                Food food = foodOptional.get();
                food.setName(updatedFood.getName());
                food.setPrice(updatedFood.getPrice());
                food.setStateGST(updatedFood.getStateGST());
                food.setCentralGST(updatedFood.getCentralGST());
                food.setTotalGST(updatedFood.getTotalGST());
                food.setTotalPrice(updatedFood.getTotalPrice());
                food.setDescription(updatedFood.getDescription());
                food.setCategoryName(updatedFood.getCategoryName());
                food.setImageUrl(updatedFood.getImageUrl());

                Food savedFood = foodRepository.save(food);
                return ResponseEntity.ok(savedFood);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating food item: " + e.getMessage());
        }
    }

    // Delete a food item by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        try {
            Optional<Food> foodOptional = foodRepository.findById(id);
            if (foodOptional.isPresent()) {
                foodRepository.deleteById(id);
                return ResponseEntity.ok("Food item successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting food item: " + e.getMessage());
        }
    }
}
